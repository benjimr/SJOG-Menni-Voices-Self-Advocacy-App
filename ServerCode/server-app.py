import vimeo
import json
import re
import io

import os
from flask import Flask
from flask_mysqldb import MySQL
import json
from flask import request
from flask import send_file
from flask import Response
from flask import make_response

dbs = MySQL()

app = Flask(__name__)

app.config['MYSQL_USER'] = 'root'
app.config['MYSQL_PASSWORD'] = 'advocacy'
app.config['MYSQL_DB'] = 'users'
app.config['MYSQL_HOST'] = '104.155.53.22'

dbs.init_app(app)

@app.route("/")
def test():
	conn = dbs.connection.cursor()
	conn.execute("select * from Users")
	return str(conn.fetchall())

@app.route("/getEvents")
def getEvents():
	conn = dbs.connection.cursor()
	conn.execute("select * from Events where eventDate > CURDATE()")
	headers = [header[0] for header in conn.description]
	resultSet = conn.fetchall()
	data = []

	if conn.rowcount <= 0:
		return 'FAILURE'

	for result in resultSet:
		data.append(dict(zip(headers,result)))

	return json.dumps(data, default=str)

@app.route("/getUsers")
def getUsers():
	conn = dbs.connection.cursor()
	conn.execute("select * from newUsers")
	headers = [header[0] for header in conn.description]
	resultSet = conn.fetchall()
	data = []

	if conn.rowcount <= 0:
		return 'FAILURE'

	for result in resultSet:
		data.append(dict(zip(headers,result)))

	return json.dumps(data, default=str)

@app.route("/returnUsers/<userName>")
def returnUsers(userName):
	conn = dbs.connection.cursor()
	conn.execute("select * from newUsers where userName = \'" + userName + "\'")
	headers =  [header[0] for header in conn.description]
	resultSet = conn.fetchall()
	data=[]

	user = " "

	for x in resultSet:
		user += str(x[0])
		user += " "
		user += str(x[1])
		user += " "
		user += str(x[2])
		user += " "
		user += str(x[3])
		user += " "

		user += str(x[4])


	if conn.rowcount <= 0:
		return 'FAILURE'

	return user

@app.route("/isAdmin/<userName>")
def isAdmin(userName):
	conn = dbs.connection.cursor()

	stmt = ("select level from newUsers where userName = \'" + userName + "\'")
	conn.execute(stmt)

	result = conn.fetchone()

	if result[0] == 1:
		return 'TRUE'
	else:
		return 'FALSE'

@app.route("/checkUserExists/<username>")
def checkUserExists(username):
	conn = dbs.connection.cursor()

	stmt = ("select * from newUsers where userName = \'" + username + "\'")
	conn.execute(stmt)

	if conn.rowcount == 1:
		return 'SUCCESS'
	else:
		return 'FAILURE'

@app.route("/changePassword/<username>/<newpass>")
def changePass(username, newpass):
        conn = dbs.connection.cursor()

        stmt = "update newUsers set password = %s where userName = %s"

        try:
                conn.execute(stmt, (newpass, username))
                dbs.connection.commit()
                return 'SUCCESS'
        except Exception as e:
                print(e)
                return 'FAILURE'

@app.route("/login/<userName>/<password>", methods=['GET'])
def login(userName, password):
	conn = dbs.connection.cursor()

	#form sql statement
	stmt = ("""select * from newUsers where userName = %s and password = %s""")
	conn.execute(stmt, (userName, password))

	if conn.rowcount == 1:
		return 'SUCCESS'
	else:
		return 'FAILURE'
"""
WORKING version - dupicated in case i break it
@app.route("/getVideos")
def getVideos():
	v = vimeo.VimeoClient(
    		token="81e7f05a8bdd4aeca46f464705c877a3",
    		key="4ae71a994fd01243ec81198daba42fe517832820",
    		secret="8UbigGyYMe1/EfEecJxL/n5QneEeSPoBm2uKN+mTZnSyn9oMWeIIuBZ6eEGV6jJnAWQOfcL4L6GPwcAVDrU7/MWso4rhJgz585I+DwQdSRLOlp+/BZ8QDk0SNDUt64Kw"
		)

	about_me = v.get('/me/videos?per_page=100&filter=playable&query=Menni&sort=date&weak_search=true', params={"fields": "link, name"})
	assert about_me.status_code == 200

	jsonOutput = about_me.json()
	string1 = str(jsonOutput)
	print(jsonOutput)
	string2 = "src="
	VideoArray = [m.start() for m in re.finditer(string2, string1)]
	response = ""

	stmt = "delete from videos"

	for i in VideoArray:
		string3 = string1[i+5:i+45]
		response += string3
		response += " "

	return response
"""

@app.route("/getVideos")
def getVideos():
	v = vimeo.VimeoClient(
    		token="81e7f05a8bdd4aeca46f464705c877a3",
    		key="4ae71a994fd01243ec81198daba42fe517832820",
    		secret="8UbigGyYMe1/EfEecJxL/n5QneEeSPoBm2uKN+mTZnSyn9oMWeIIuBZ6eEGV6jJnAWQOfcL4L6GPwcAVDrU7/MWso4rhJgz585I+DwQdSRLOlp+/BZ8QDk0SNDUt64Kw"
		)

	about_me = v.get('/me/videos?per_page=100&filter=playable&query=Menni&sort=date&weak_search=true', params={"fields": "link, name, pictures"})
	assert about_me.status_code == 200
	jsonData = about_me.json()
	strData = json.dumps(jsonData)

	dataLoc = strData.find("data") + 7
	strData = strData[dataLoc:len(strData)]
	return strData


@app.route("/register/<userName>/<password>")
def register(userName, password):
	conn = dbs.connection.cursor()

	#form sql statement
	stmt = ("""insert into Users(userName, password, level) values(%s, %s, 0)""")

	try:
		conn.execute(stmt, (userName, password))
		dbs.connection.commit()
		return 'SUCCESS'
	except Exception as e:
		print(e)
		return 'FAILURE'


@app.route("/newRegister/<username>/<firstname>/<surname>/<password>")
def newRegister(username, firstname, surname, password):
        conn = dbs.connection.cursor()

	#form sql statement
        stmt = ("""insert into newUsers(userName, firstName, surname, password) values (%s, %s, %s, %s)""")

        try:
                conn.execute(stmt, (username, firstname, surname, password))
                dbs.connection.commit()
                return 'SUCCESS'
        except Exception as e:
                print(e)
                return 'FAILURE'


@app.route("/createEvent/<eventName>/<eventDesc>/<eventDate>")
def createEvent(eventName, eventDesc, eventDate):
	conn = dbs.connection.cursor()
	print(eventName + " " + eventDesc + " " + eventDate)
	stmt = """insert into Events(eventName, eventDesc, eventDate) value(%s, %s, %s)"""

	try:
		conn.execute(stmt, (eventName, eventDesc, eventDate))
		dbs.connection.commit()
		return 'SUCCESS'
	except Exception as e:
		print(e)
		return 'FAILURE'

@app.route("/deleteEvent/<id>")
def deleteEvent(id):
	conn = dbs.connection.cursor()

	stmt = "delete from Events where eventID = \'" + id + "\'"

	try:
		conn.execute(stmt)
		dbs.connection.commit()
		return 'SUCCESS'
	except Exception as e:
		print(e)
		return 'FAILURE'

@app.route("/editEvent/<eventID>/<eventName>/<eventDesc>/<eventDate>")
def editEvent(eventID, eventName, eventDesc, eventDate):
	conn = dbs.connection.cursor()

	stmt = """update Events set eventName = %s, eventDesc = %s, eventDate = %s where eventID = %s """

	try:
		conn.execute(stmt, (eventName, eventDesc, eventDate, int(eventID)))
		dbs.connection.commit()
		return 'SUCCESS'
	except Exception as e:
		print(e)
		return 'FAILURE'

@app.route("/changeUsersPassword/<id>/<password>")
def changeUsersPassword(id, password):
	conn = dbs.connection.cursor()

	stmt = "update newUsers set password = %s where userID = %s"

	try:
		conn.execute(stmt, (password, id))
		dbs.connection.commit()
		return 'SUCCESS'
	except Exception as e:
		print(e)
		return 'FAILURE'

@app.route("/toggleAdmin/<id>")
def toggleAdmin(id):
	conn = dbs.connection.cursor()

	stmt = "select * from newUsers where userID = \'" + id + "\' and level = 1"

	conn.execute(stmt)

	#if admin
	if conn.rowcount == 1:
		stmt = "update newUsers set level = 0 where userID = \'" + id + "\'"
	else:
		stmt = "update newUsers set level = 1 where userID = \'" + id + "\'"

	try:
		conn.execute(stmt)
		dbs.connection.commit()
		return 'SUCCESS'
	except Exception as e:
		print(e)
		return 'FAILURE'

@app.route("/deleteUser/<id>")
def deleteUser(id):
	conn = dbs.connection.cursor()

	stmt = "delete from newUsers where userID = \'" + id + "\'"

	try:
		conn.execute(stmt)
		dbs.connection.commit()
		return 'SUCCESS'
	except Exception as e:
		print(e)
		return 'FAILURE'

@app.route("/uploadImage/<id>", methods = ['POST'])
def uploadImage(id):
	try:

		data = request.stream.read()

		filename = id + ".png"
		print(filename)
		file = open(filename, "wb")
		file.write(data)
		file.close()
		return 'SUCCESS'
	except Exception as e:
		print(e)
		return 'FAILURE'


@app.route("/getProfileImg/<id>")
def getProfileImg(id):
	name = id + ".png"

	if(os.path.isfile(name)):
		file = open(name, "rb")
		data = file.read()
		file.close()
		response = make_response(data)
		response.headers.set('Content-type', 'image/png')
		response.headers.set('Content-Disposition', 'attachment', filename=name)
		return response
	else:
		file = open("default.png", "rb")
		data = file.read()
		file.close()
		response = make_response(data)
		response.headers.set('Content-type', 'image/png')
		response.headers.set('Content-Disposition', 'attachment', filename=name)
		return response

if __name__ ==  "__main__":
	app.run(host='0.0.0.0', port=5000)
