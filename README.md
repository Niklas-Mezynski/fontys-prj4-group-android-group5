# prj4-group-android-group5
<table>
<tr><th>
    Non Functional Requirement
</th>
<th>
    Realized by
</th>
</tr>
<tr>
    <td>Camera usage</td>
    <td><ul><li>Images of the party</li> <li>profile picture</li> <li>entrance qr code</ul></td>
</tr>
<tr>
    <td>Geolocation</td>
    <td><ul>
        <li>Finding partys in radius incl. address</li>
        <li>Location of party members, in case they gets lost</li>
    </ul></td>
</tr>
<tr>
    <td>Sending and recieving push notification</td>
    <td><ul>
        <li>Notification, when you were accepted to a party</li>
        <li>Notification, when request received</li>
        <li></li>
    </ul></td>
</tr>
<tr>
    <td>Restful api</td>
    <td><ul>
        <li>Access centralized server</li>
        <li></li>
    </ul></td>
</tr>
</table>


# Setup

## Database

- install docker (For windows also: WSL 2)
- command: docker pull postgres
- run postgres: docker run --name postgresPrj4 -e POSTGRES_PASSWORD=prj4Group5 -d -p 5432:5432 postgres

