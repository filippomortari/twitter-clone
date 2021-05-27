import { Avatar, Button } from "@material-ui/core";
import React, { useEffect, useState } from "react";
import { Auth } from 'aws-amplify'
import "./TweetBox.css";

const TweetBox = (props) => {
  const [tweetMessage, setTweetMessage] = useState("");
  const [tweetImage, setTweetImage] = useState("");

  const sendTweet = (e) => {
    e.preventDefault();

    setTweetMessage("");
    setTweetImage("");
  };

  const [profileImage, setProfileImage] = useState("");

  useEffect(() => {
    Auth
      .currentUserInfo()
      .then((userInfo) => { setProfileImage(`/static/avatar/${userInfo.username}.jpeg`) })
      .catch(err => {
        console.log('Error : ' + err);
      })
  }, [props.authState]);

  return (
    <div className="tweetBox">
      <form>
        <div className="tweetBox__input">
          <Avatar src={profileImage} />
          <input
            value={tweetMessage}
            onChange={(e) => setTweetMessage(e.target.value)}
            placeholder="What's happening?"
            type="text"
          />
        </div>
        <Button onClick={sendTweet} type="submit" className="tweetBox__button">
          Tweet
        </Button>
      </form>
    </div>
  );
}

export default TweetBox;
