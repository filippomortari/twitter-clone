import React from "react";
import "./Sidebar.css";
import SidebarOption from "./SidebarOption";
import TwitterIcon from "@material-ui/icons/Twitter";
import HomeIcon from "@material-ui/icons/Home";
import PermIdentityIcon from "@material-ui/icons/PermIdentity";

import {
  BrowserRouter as Router,
  Switch,
  Route,
  NavLink,
  Redirect
} from "react-router-dom";

import Feed from './Feed'

const routes = [
  {
    path: "/",
    exact: true,
    main: () => <Redirect to="/timeline" />
  },
  {
    path: "/timeline",
    main: (authState) => <Feed authState={authState} feedType="timeline" />
  },
  {
    path: "/profile",
    main: (authState) => <Feed authState={authState} feedType="profile" />
  }
];

const Sidebar = (props) => {
  return (
    <Router>
      <>
        <div className="sidebar">
          <TwitterIcon className="sidebar__twitterIcon" />
          <NavLink className="sidebarOption" activeClassName="sidebarOption--active" to="/timeline"><SidebarOption Icon={HomeIcon} text="My friends tweets" /></NavLink>
          <NavLink className="sidebarOption" activeClassName="sidebarOption--active" to="/profile"><SidebarOption Icon={PermIdentityIcon} text="My tweets" /></NavLink>
        </div>

        <Switch>
          {routes.map((route, index) => (
            <Route
              key={index}
              path={route.path}
              exact={route.exact}
              children={<route.main authState={props.authState} />}
            />
          ))}
        </Switch>
      </>
    </Router>
  );
}

export default Sidebar;
