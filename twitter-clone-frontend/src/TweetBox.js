import { Avatar, Button } from "@material-ui/core";
import React, { useEffect, useState } from "react";
import { Auth } from 'aws-amplify';
import { useHistory } from "react-router-dom";

import "./TweetBox.css";

import { postTweet } from './utils/fetcher'

const TweetBox = (props) => {
  const [tweetMessage, setTweetMessage] = useState("");
  const [disabled, setDisabled] = useState("");
  const [errMessage, setErrMessage] = useState("")

  const history = useHistory();

  const sendTweet = (e) => {
    e.preventDefault();

    const payload = {content: tweetMessage};

    Auth
      .currentSession()
      .then(tokens => tokens.accessToken.jwtToken)
      .then((token) => postTweet(token, payload))
      .then((result) => {
        setTweetMessage("");
        setDisabled("");
        setErrMessage("");

        history.push("/profile");
      })
      .catch(err => {
        console.log('Error : ' + err);
        setErrMessage(err)
      })

    
  };

  const handleInput= (text) => {
    setTweetMessage(text);

    if (text.length > 160) {
      setDisabled("disabled");
      setErrMessage("Tweet length cannot exceed 160 characters.")
    } else {
      setDisabled("");
      setErrMessage("")
    }
  }

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
      <div className="errMessage">{errMessage}</div>
      <form>
        <div className="tweetBox__input">
          <Avatar src={profileImage} />
          <input
            value={tweetMessage}
            onChange={(e) => handleInput(e.target.value)}
            placeholder="What's happening?"
            type="text"
          />
        </div>
        <Button onClick={sendTweet} disabled={disabled} type="submit" className="tweetBox__button">
          Tweet
        </Button>
      </form>
    </div>
  );
}

export default TweetBox;
