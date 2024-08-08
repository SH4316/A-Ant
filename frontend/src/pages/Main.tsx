import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";

function MainPage() {
    const navigation = useNavigate();
    const [postId, setPostId] = useState<string>("");
    const [editorId, setEdiorId] = useState<string>("");

    return <>
        <ul>
            <li>
                <Link to="/login">login</Link>
            </li>
            <li>
                <Link to="/signup">signup</Link>
            </li>
            <li>
                <Link to={"/post/" + postId}>post</Link>
                <input type="text" name="post" id="post" onChange={(e) => {setPostId(e.target.value)}} />
            </li>
            <li>
                <p onClick={() => {
                    fetch("/api/v1/post", {
                        method: "POST",
                        body: JSON.stringify({
                            title: "",
                            body: "",
                        })
                    }).then((v) => {
                        if (v.status == 201) {
                            return v.text()
                        }
                        throw v.statusText;
                    }).then((v) => {
                        window.history.pushState(null, "New Page Title", "/editor/" + v);
                        navigation(0);
                        // window.history.back()
                    }).catch(err => {
                        console.log(err)
                    })
                }}>create post</p>
            </li>
            <li>
                <Link to="/mypost">my posts</Link>
            </li>
            <li>
                <Link to="/editor">new post</Link>
            </li>
            <li>
                <Link to={"/editor/" + editorId}>editor</Link>
                <input type="number" name="editor" id="editor" onChange={(e) => {setEdiorId(e.target.value)}} />
            </li>
        </ul>
    </>
}

export default MainPage;