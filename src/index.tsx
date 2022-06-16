import React from 'react';
import ReactDOM from 'react-dom';
import App from './App';

window.onload = () => {
    const rootElement: HTMLElement = document.getElementById("root") as HTMLInputElement;

    ReactDOM.render(
        <React.StrictMode>
            <App pageWidth={rootElement.clientWidth} pageHeight={rootElement.clientHeight} />
        </React.StrictMode>,
        rootElement
    );
};
