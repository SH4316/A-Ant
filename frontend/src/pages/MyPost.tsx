import React, { useState, useEffect } from 'react';
import { jwtDecode } from 'jwt-decode';
import { useNavigate } from 'react-router-dom';

interface Post {
    id: string;
    title: string;
    body: string;
}

interface JwtPayload {
    id: string;
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    [key: string]: any;
}

const PostsPage: React.FC = () => {
    const navigation = useNavigate();
    const [posts, setPosts] = useState<Post[]>([]);
    const [error, setError] = useState<string | null>(null);

    const getJwtFromCookie = (): string | null => {
        const cookies = document.cookie.split(';');
        for (const cookie of cookies) {
        const [name, value] = cookie.trim().split('=');
        if (name === 'JWT') {
            return decodeURIComponent(value);
        }
        }
        return null;
    };

    useEffect(() => {
        const fetchPosts = async () => {
            try {
                // Get the JWT token from the cookie
                const token = getJwtFromCookie();
                
                if (!token) {
                    throw new Error('No JWT token found in cookie');
                }

                // Decode the JWT token to get the user ID
                const decodedToken = jwtDecode<JwtPayload>(token);
                const userId = decodedToken.id;
                const header = new Headers();
                header.append("Cookie", `JWT=${token}`);

                // Fetch posts from the API
                const response = await fetch(`/api/v1/post/all?user=${encodeURIComponent(userId)}`, {
                    headers: header,
                    credentials: 'include', // This is important for including cookies in the request
                });

                if (!response.ok) {
                    throw new Error('Failed to fetch posts');
                }

                const data: {verified: boolean, posts: Post[]} = await response.json();
                console.log(data)
                setPosts(data.posts);
            } catch (err) {
                setError(err instanceof Error ? err.message : 'An unknown error occurred');
            }
        };

        fetchPosts();
    }, []);

    if (error) {
        return <div>Error: {error}</div>;
    }

    return (
        <div>
            <h1>Posts</h1>
            {posts.map(post => (
                <div key={post.id} style={{height: "100px", width: "100%", backgroundColor: "gray"}} >
                    <h2 onClick={() => {
                        window.history.pushState(null, post.title, `/post/${post.id}`);
                        navigation(0);
                    }}>{post.title ? post.title : "제목이 없습니다"}</h2>
                    <p>{post.body ? post.body : "내용이 없습니다"}</p>
                    <button type="button" onClick={() => {
                        window.history.pushState(null, post.title, `/editor/${post.id}`);
                        navigation(0);
                    }}>edit</button>
                </div>
            ))}
        </div>
    );
};

export default PostsPage;