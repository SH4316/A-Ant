import React, { useState } from 'react';

interface LoginFormData {
    email: string;
    password: string;
}

const LoginPage: React.FC = () => {
    const [formData, setFormData] = useState<LoginFormData>({
        email: '',
        password: '',
    });

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData((prevData) => ({
        ...prevData,
        [name]: value,
        }));
    };

    const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        fetch("/api/v1/user/login", 
            {
                method: "POST",
                body: JSON.stringify(
                    {
                        email: formData.email,
                        pw: formData.password
                    }
                ), 
                headers: {
                    "Content-Type": "application/json"
                }
            }
        )
        .then((value) => {
            value.status
        })
        console.log('Login attempt with:', formData);
    };

    const handleSignUp = () => {
        // Here you would typically navigate to the sign-up page or open a sign-up modal
        console.log('Sign Up button clicked');
    };

    return (
        <div style={{ maxWidth: '300px', margin: '0 auto', padding: '20px' }}>
        <h1>Login</h1>
        <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
            <div>
            <label htmlFor="email">Email:</label>
            <input
                type="text"
                id="email"
                name="email"
                value={formData.email}
                onChange={handleInputChange}
                required
                style={{ width: '100%', padding: '5px' }}
            />
            </div>
            <div>
            <label htmlFor="password">Password:</label>
            <input
                type="password"
                id="password"
                name="password"
                value={formData.password}
                onChange={handleInputChange}
                required
                style={{ width: '100%', padding: '5px' }}
            />
            </div>
            <button type="submit" style={{ padding: '10px', backgroundColor: '#007bff', color: 'white', border: 'none', cursor: 'pointer' }}>
            Login
            </button>
        </form>
        <button 
            onClick={handleSignUp} 
            style={{ 
            marginTop: '10px', 
            width: '100%', 
            padding: '10px', 
            backgroundColor: '#28a745', 
            color: 'white', 
            border: 'none', 
            cursor: 'pointer' 
            }}
        >
            회원가입
        </button>
        </div>
    );
};

export default LoginPage;