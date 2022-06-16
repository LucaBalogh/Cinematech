import React from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import { AuthProvider } from "./auth/AuthProvider";
import Login from "./auth/Login";
import Locations from "./locationpage/Locations";
import { IAppProps } from "./model/IAppProps";
import "./styles/bulma.css";
import "./styles/custom.css";
import MainPage from "./main/MainPage";
import { LocationProvider } from "./locationpage/locationAll/LocationProvider";


const App = (props: IAppProps) => {
    return (
        <AuthProvider>
            <LocationProvider>
                <BrowserRouter>
                    <Routes>
                        <Route path="/login" element={<Login />} />
                        <Route path="/dashboard" element={<MainPage pageHeight={props.pageHeight} pageWidth={props.pageWidth}  />} />
                        <Route path="/locations" element={<Locations pageHeight={props.pageHeight} pageWidth={props.pageWidth} />} />
                        <Route path="/" element={<Login/>}/>
                    </Routes>
                </BrowserRouter>
            </LocationProvider>
        </AuthProvider>
    );
};

export default App;
