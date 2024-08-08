import React, { useState } from 'react';

interface SignUpFormData {
  email: string;
  password: string;
}

const SignUpPage: React.FC = () => {
    const [formData, setFormData] = useState<SignUpFormData>({
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
        const header = new Headers();
        header.delete("Cookie");
        // header.set
        fetch("/api/v1/user/register", {
            method: "POST",
            headers: header,
            body: JSON.stringify({
                email: formData.email,
                pw: formData.password
            })
        })
        // .then((value) => {
        //     console.log(value.json())
        // })

        console.log('Sign up attempt with:', formData);
    };

    return (
        <div>
        <h1>회원가입</h1>
        <form onSubmit={handleSubmit}>
            <div>
            <label htmlFor="email">Email:</label>
            <input
                type="email"
                id="email"
                name="email"
                value={formData.email}
                onChange={handleInputChange}
                required
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
            />
            </div>
            <button type="submit">가입하기</button>
        </form>
        </div>
    );
};

export default SignUpPage;