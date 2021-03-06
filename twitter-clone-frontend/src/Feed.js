import React, { useEffect, useState } from "react";
import { Auth } from 'aws-amplify'
import "./Feed.css";
import Post from "./Post";
import TweetBox from "./TweetBox";
import { getProfile, getTimeline } from './utils/fetcher'
import ProfileBox from "./ProfileBox";


const Feed = (props) => {
  const [posts, setPosts] = useState([]);

  useEffect(() => {
    const getTweets = props.feedType === 'timeline' ? getTimeline : getProfile

    Auth
      .currentSession()
      .then(tokens => tokens.accessToken.jwtToken)
      .then((token) => getTweets(token))
      .then((result) => setPosts(result.data.content))
      .catch(err => {
        console.log('Error : ' + err);
      })
  }, [props.authState, props.feedType]);

  return (
    <div className="feed">
      <div className="feed__header">
        <h2>{props.feedType === 'timeline' ? 'Home' : 'Profile' }</h2>
      </div>
      {props.feedType === 'timeline' && <TweetBox authState={props.authState} />}
      {props.feedType === 'profile' && <ProfileBox authState={props.authState} />}
      {posts.map((post) => (
        <Post
          displayName={`${post.author.firstName} ${post.author.lastName}`}
          username={post.author.username}
          verified={true}
          text={post.content}
          avatar={`/static/avatar/${post.author.username}.jpeg`}
          image={post.image}
          key={post.id}
          dateTime={post.createdDate}
        />
      ))}
    </div>
  );
}

export default Feed;
