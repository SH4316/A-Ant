import './App.css'
import LoginPage from './pages/Login'
import { BrowserRouter, Route, Routes } from 'react-router-dom'
import MarkdownEditor from './pages/Editor'
import SignUpPage from './pages/SignUp'
import PostViewer from './pages/PostViewer'
import MainPage from './pages/Main'
import MyPost from './pages/MyPost'

function App() {

  return (
    <>
      <BrowserRouter>
        <Routes>
          <Route path='/' element={<MainPage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/signup" element={<SignUpPage />} />
          <Route path='/mypost' element={<MyPost />}/>
          <Route path="/post/:id" element={<PostViewer />} />
          <Route path="/editor/:id" element={<MarkdownEditor />} />
        </Routes>
      </BrowserRouter>
    </>
  )
}

export default App
