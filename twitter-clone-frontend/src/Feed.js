import React, { useEffect, useState } from "react";
import { Auth } from 'aws-amplify'
import "./Feed.css";
import Post from "./Post";
import TweetBox from "./TweetBox";
import { getProfile } from './utils/fetcher'


const Feed = (props) => {
  const [posts, setPosts] = useState([]);

  useEffect(() => {
      Auth
      .currentSession()
      .then(tokens => tokens.accessToken.jwtToken)
      .then((token) => getProfile(token))
      .then((result) => setPosts(result.data.content))
      .catch(err => {
        console.log('Error : ' + err);
      })
  }, [props.authState]);

  return (
    <div className="feed">
      <div className="feed__header">
        <h2>Home</h2>
      </div>
      <TweetBox authState={props.authState} />
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
