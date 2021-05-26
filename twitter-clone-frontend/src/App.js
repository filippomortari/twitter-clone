// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT

import React from 'react'
import { Amplify } from 'aws-amplify'
import { AmplifyAuthenticator, AmplifySignOut, AmplifySignIn } from '@aws-amplify/ui-react'
import { onAuthUIStateChange } from '@aws-amplify/ui-components'
import { Button } from "@material-ui/core";

import config from './config'

import Feed from "./Feed";
import Sidebar from "./Sidebar";
import "./App.css";


var awsconfig = {}
awsconfig.Auth = {
  region: config.regionID,
  userPoolId: config.userPoolId,
  userPoolWebClientId: config.userPoolClientId
}
Amplify.configure(awsconfig)

var MyPropsMethods = {}

const App = () => {
  return (
    <AuthWrapper />
  )
}

const AuthWrapper = () => {

  const [authState, setAuthState] = React.useState()
  const [showLogin, setShowLogin] = React.useState(false)
  const goToSignIn = () => setShowLogin(true)
  const exitFromSignIn = () => setShowLogin(false)
  const showAuthenticator = (showLogin || authState === 'signedin')
  const showApp = (!showLogin || authState === 'signedin')
  const showgoToSignIn = !(showLogin || authState === 'signedin') && authState != null

  React.useEffect(() => {
    return onAuthUIStateChange(newAuthState => {
      setAuthState(newAuthState)
      if (newAuthState === 'signedin') {
        setShowLogin(false)
      }
    })
  }, [])

  return (
    <div>
      <div style={{
        display: showAuthenticator ? '' : 'none',
        textAlign: 'center'
      }}>
        <AmplifyAuthenticator usernameAlias="username">
          <AmplifySignIn slot="sign-in" hideSignUp="true" 
          formFields={[
            { type: "username" },
            { type: "password", hint: '' }
          ]}
          />
          <AmplifySignOut />
        </AmplifyAuthenticator>
      </div>

      { showApp &&
        <div style={{ textAlign: 'center', margin: '0 0 0 0' }}>
          {showgoToSignIn &&
            <Button variant="outlined" onClick={goToSignIn} className="loginButton" fullWidth>SIGN IN</Button>
          }
          {showgoToSignIn !== true &&
            <div className="container">
              <Sidebar />
              <Feed authState={authState} />
            </div>
          }
        </div>
      }

    </div>
  )

}

export default App