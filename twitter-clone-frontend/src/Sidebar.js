import React from "react";
import "./Sidebar.css";
import SidebarOption from "./SidebarOption";
import TwitterIcon from "@material-ui/icons/Twitter";
import HomeIcon from "@material-ui/icons/Home";
import PermIdentityIcon from "@material-ui/icons/PermIdentity";

import Feed from './Feed'

// const routes = [
//   {
//     path: "/",
//     exact: true,
//     sidebar: () => <div>home!</div>,
//     main: () => <h2>Home</h2>
//   },
//   {
//     path: "/bubblegum",
//     sidebar: () => <div>bubblegum!</div>,
//     main: () => <h2>Bubblevxczgum</h2>
//   },
//   {
//     path: "/shoelaces",
//     sidebar: () => <div>shoelaces!</div>,
//     main: () => <h2>Shoelaces</h2>
//   }
// ];

const Sidebar = (props) => {
  return (
    <>
      <div className="sidebar">
        <TwitterIcon className="sidebar__twitterIcon" />
        <SidebarOption Icon={HomeIcon} text="My friends tweets" active={true} />
        <SidebarOption Icon={PermIdentityIcon} text="My tweets" />

      </div>
      <Feed authState={props.authState} feedType="profile" />
    </>

  );
}

export default Sidebar;
