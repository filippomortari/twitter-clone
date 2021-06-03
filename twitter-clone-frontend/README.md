# twitter-clone-frontend
This is a simple React application deployed as a static webapp via AWS Amplify (https://aws.amazon.com/amplify/). The app is built and deployed via a CI/CD pipeline that can be configured through AWS Amplify Console. The aforementioned pipeline is triggered automatically by each commit to this repo.

The build settings are stored in `./amplify.yml`

The app uses React Router to handle sidebar navigation and React Hooks for state management. It also uses components from https://material-ui.com/ for quick styling.

### Build the app and run locally
```bash
npm i

npm run start

npm run build
```

#### caveats:
The frontend app has no tests because YOLO 😅🙈

