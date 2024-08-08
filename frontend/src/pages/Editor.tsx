import React, { useState, useCallback, useRef, useEffect } from 'react';
import ReactMarkdown from 'react-markdown';
import { useParams } from 'react-router-dom';
import { Post } from './PostViewer';

const MarkdownEditor: React.FC = () => {
  const { id } = useParams();
  const [title, setTitle] = useState<string>("Error");
  const [markdownText, setMarkdownText] = useState<string>('');
  const [editorWidth, setEditorWidth] = useState<number>(50); // 50% initial width
  const containerRef = useRef<HTMLDivElement>(null);
  const resizerRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    fetch("/api/v1/post/" + id, {
      // method: "GET"
    }).then((v) => {
        if (v.status == 200) {
            return v.json()
        }
        // TODO : redirect to main
    }).then((j: Post) => {
        setTitle(j.title)
        setMarkdownText(j.body);
    })
  }, [id]);

  const handleTitleInputChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    setTitle(e.target.value);
  };

  const handleInputChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    setMarkdownText(e.target.value);
  };

  const handleExit = () => {
    console.log('Exit button clicked');
    // Add logic for exiting
  };

  const handleSaveDraft = () => {
    // console.log('Save Draft button clicked');
    // console.log('Current draft:', markdownText);

    const header = new Headers();
    // header.append("Post-Author", );
    
    header.append("Post-ID", id ? encodeURI(id) : "");
    header.append("Post-Title", encodeURI(title));
    header.append("Post-Body", encodeURI(markdownText));
    
    fetch("/api/v1/post", {
      method: "PUT",
      headers: header
    }).then((data) => {
      if (data.status == 200) {
        alert("Succeed to save data!")
      }
    })
  };

  const handlePublish = () => {
    console.log('Publish button clicked');
    console.log('Content to publish:', markdownText);
  };

  const handleMouseDown = useCallback((e: React.MouseEvent) => {
    e.preventDefault();
    document.addEventListener('mousemove', handleMouseMove);
    document.addEventListener('mouseup', handleMouseUp);
  }, []);

  const handleMouseMove = useCallback((e: MouseEvent) => {
    if (containerRef.current) {
      const containerRect = containerRef.current.getBoundingClientRect();
      const newWidth = ((e.clientX - containerRect.left) / containerRect.width) * 100;
      setEditorWidth(Math.min(Math.max(newWidth, 20), 80)); // Limit between 20% and 80%
    }
  }, []);

  const handleMouseUp = useCallback(() => {
    document.removeEventListener('mousemove', handleMouseMove);
    document.removeEventListener('mouseup', handleMouseUp);
  }, [handleMouseMove]);

  return (
    <div ref={containerRef} style={{ display: 'flex', height: '100vh', position: 'relative' }}>
      <div style={{ width: `${editorWidth}%`, padding: '10px', display: 'flex', flexDirection: 'column' }}>
        <textarea
          value={title}
          onChange={handleTitleInputChange}
          style={{
            width: '100%',
            height: '30px',
            resize: 'none',
            fontFamily: 'monospace',
            marginBottom: '10px',
          }}
        />
        {/* <h2>title:{title}</h2> */}
        <textarea
          value={markdownText}
          onChange={handleInputChange}
          style={{
            width: '100%',
            flex: 1,
            resize: 'none',
            fontFamily: 'monospace',
            marginBottom: '10px',
          }}
        />
        <div style={{ display: 'flex', justifyContent: 'space-between' }}>
          <button onClick={handleExit}>나가기</button>
          <button onClick={handleSaveDraft}>임시저장</button>
          <button onClick={handlePublish}>출간</button>
        </div>
      </div>
      <div
        ref={resizerRef}
        style={{
          width: '5px',
          background: '#ccc',
          cursor: 'col-resize',
          zIndex: 1,
        }}
        onMouseDown={handleMouseDown}
      />
      <div style={{ width: `${100 - editorWidth}%`, padding: '10px', overflowY: 'auto' }}>
        <h2>Preview</h2>
        <ReactMarkdown>{markdownText}</ReactMarkdown>
      </div>
    </div>
  );
};

export default MarkdownEditor;