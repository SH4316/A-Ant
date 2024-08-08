import React, { useState, useRef, useEffect } from 'react';
import ReactMarkdown from 'react-markdown';
import { useParams } from 'react-router-dom';

export type Comment = {

}

export type Post = {
    id: number;
    title: string;
    body: string;
    comments: Comment[];
}

const PostViewer: React.FC = () => {
    const { id } = useParams();
    const [title, setTitle] = useState<string>('');
    const [markdownText, setMarkdownText] = useState<string>('# Hello, Markdown!');
    const containerRef = useRef<HTMLDivElement>(null);

    useEffect(() => {
        fetch("/api/v1/post/" + id, {
            // method: "GET"
        }).then((v) => {
            if (v.status == 200) {
                return v.json()
            }
            // TODO : redirect to main
        }).then((j: Post) => {
            setMarkdownText(j.body);
            setTitle(j.title);
        })
    }, [id]);

    return (
        <div ref={containerRef} style={{ display: 'flex', height: '100vh', position: 'relative' }}>
            <div style={{ width: `100 %`, padding: '10px', overflowY: 'auto' }}>
                <h1>{title}</h1>
                <ReactMarkdown>{markdownText}</ReactMarkdown>
            </div>
        </div>
    );
};

export default PostViewer;