import React from "react";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import {AuthProvider} from "./auth/AuthProvider";
import Login from "./auth/Login";
import Movies from "./moviepage/Movies";
import {IAppProps} from "./model/IAppProps";
import "./styles/bulma.css";
import "./styles/custom.css";
import MainPage from "./main/MainPage";
import {MovieProvider} from "./moviepage/movieAll/MovieProvider";


const App = (props: IAppProps) => {
    return (
        <AuthProvider>
            <MovieProvider>
                <BrowserRouter>
                    <Routes>
                        <Route path="/login" element={<Login />} />
                        <Route path="/dashboard" element={<MainPage pageHeight={props.pageHeight} pageWidth={props.pageWidth}  />} />
                        <Route path="/movies" element={<Movies pageHeight={props.pageHeight} pageWidth={props.pageWidth} />} />
                        <Route path="/" element={<Login/>}/>
                    </Routes>
                </BrowserRouter>
            </MovieProvider>
        </AuthProvider>
    );
};

export default App;
