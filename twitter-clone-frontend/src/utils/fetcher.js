import axios from 'axios';
import config from '../config'

export const getTimeline = (jwtToken) => axios({
    method: 'GET',
    url: `${config.apiBaseUrl}/tweets/timeline`,
    headers: { Authorization: `Bearer ${jwtToken}` }
});

export const getProfile = (jwtToken) => axios({
    method: 'GET',
    url: `${config.apiBaseUrl}/tweets/profile`,
    headers: { Authorization: `Bearer ${jwtToken}` }
});

export const postTweet = async (jwtToken, payload) => axios({
    method: 'POST',
    url: `${config.apiBaseUrl}/tweets`,
    data: payload,
    headers: { Authorization: `Bearer ${jwtToken}` }
});