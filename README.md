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

NOTE:-  Since the notes are to be stored on a  server , therefore right now I have limited the maximum number  of notes ,a user can make ,  to 3, but the limit can be extended to a large number by just increasing the number of columns on the sql database stored on server

 4.  server details:-

    hosting site   :- www.hostinger.in
    server name :- chaipeeo.esy.es

5.  other technologies used: php
