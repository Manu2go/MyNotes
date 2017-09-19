# MyNotes
Developed an android app named 'MyNotes' for note making. Since it has a user login system and all the notes are stored on web server,  therefore a person can view his notes on any mobile using his username and password .

Some details of my app:-


1.  two fragments:-
        
          a. "listentry"        :- for adding notes
          b. "lisupdate"      :- for updating and deleting notes

     these fragments are used in the activity named "Notes"

2.  four activities:-
          a.  "splashscreen" :- the welcome screen
          b.  "login"                :- for sign in  uses  POST request
          c.  "Register"          :- for sign up uses GET request
          d.  "Notes"              :- shows the main content

3.  since the app has a user login system,
     therefore all the usernames, passwords and  all the notes made by a user
     are stored on an SQL database stored on server .


4.  other technologies used: php

# Screenshots
<div>
<img src="/Screenshots/3.png" alt="Drawing"  height="300" width="180" hspace="20">
<img src="/Screenshots/1.png" alt="Drawing"  height="300" width="180" hspace="20">
<img src="/Screenshots/2.png" alt="Drawing"  height="300" width="180" hspace="20">
<br/><br/>
<img src="/Screenshots/11.png" alt="Drawing"  height="300" width="180" hspace="20">
<img src="/Screenshots/10.png" alt="Drawing"  height="300" width="180" hspace="20">
<img src="/Screenshots/20.png" alt="Drawing"  height="300" width="180" hspace="20">
<br/><br/>
<img src="/Screenshots/21.png" alt="Drawing"  height="300" width="180" hspace="20">
</div>

## Note:- 
Sometimes, it may happen that a user might not be able to login or some other operations (requiring server calls) may result with an unsuccessful status, this might be possible due to poor functioning of servers.

Therefore, if a user encounters such problems frequently then he should follow the given steps:-

1. create a local server or choose any remote server (to store php files and to create a MySQL database)
2. download the zip file(containing all php files) from the link:-
  https://drive.google.com/open?id=0B8i_i9ZwP1yIeGJJNnpPdjhlNG8
  and put all the contents of the downloaded zip file in the htdocs(=or public_html) folder on his server
3. download the .sql file from the link:- https://drive.google.com/open?id=0B8i_i9ZwP1yIRTZyWFNKWDM4c3c 
   and create a database from that file
4. add the details of his newly created database in dbconnect_notes.php file present inside the htdocs(=or public_html) folder on his server
5. and then go to app->src->main->java->manan->mynotes-->URLs.java 
	and change the URLs accordingly
