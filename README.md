# AndroidServicesShutdown
Automates timed services shutdown.

User input : 
1)Insert time in minutes : t minutes

2)Wifi: select in order to switch off wifi after t minutes.

3)Bluetooth: select in order to switch off bluetooth after t minutes.

4)Kill all task: In order to use this functionality make sure app usage is allowed for this app. This functionality kills all the background
processes which are launchable, so it does not kill the system applicaitons. This should be used in conjuction with wifi because in android
processes re-start on their own even after closing them, so its better to use it when you are turing off wifi so apps wont start consuming
resources again.

5)Silent phone: In android Do not Disturb permission are needed to restrict the notification sound. Notification level 3 is used so that 
applicaiton that use alarm type notificaiton are allowed.

6)Lock Screen: This locks the screen aftere t minutes, required to make application admin first. 

7)Stop Music: Takes control of audio manager after t min thus stopping music.
