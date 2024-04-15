import {LazyLog, ScrollFollow} from "react-lazylog";

function App() {
    const path = encodeURIComponent("D:\\demo.log")
    let url = "ws://" + location.hostname + ":8080/api/log-view?path=" + path;
    return <div style={{width: '100%', height: 400}}>
        <ScrollFollow
            startFollowing={true}
            render={({follow, onScroll}) => <LazyLog websocket url={url} stream follow={follow} onScroll={onScroll}/>}
        />
    </div>;
}

export default App;
