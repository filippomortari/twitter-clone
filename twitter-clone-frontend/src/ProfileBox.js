import { Avatar } from "@material-ui/core";
import {
  VerifiedUser
} from "@material-ui/icons";
import React, { useEffect, useState } from "react";
import { Auth } from 'aws-amplify'
import "./ProfileBox.css";

const ProfileBox = (props) => {
  const [profileImage, setProfileImage] = useState("");
  const [username, setUsername] = useState("");

  useEffect(() => {
    Auth
      .currentUserInfo()
      .then((userInfo) => { 
        console.log(userInfo)
        setProfileImage(`/static/avatar/${userInfo.username}.jpeg`) 
        setUsername(userInfo.username)
        
      })
      .catch(err => {
        console.log('Error : ' + err);
      })
  }, [props.authState]);

  return (
    <div className="tweetBox">
        <div className="profileBox">
        <div className="post__avatar">
        <Avatar src={profileImage} style={{ height: '128px', width: '128px' }}/>
        </div>
        <div className="profileBoxUser">
        
        <h1>@{username}<VerifiedUser /></h1>
        </div>
          
        </div>
        
    </div>
  );
}

export default ProfileBox;
