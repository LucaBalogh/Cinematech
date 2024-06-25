import React, {FormEvent, useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {useAuth} from "./AuthProvider";
import LoginFoot from "../images/foot.svg";
import {loggedUser} from "../utils/utilsMethods";

interface LoginState {
  email?: string;
  password?: string;
}

const Login: React.FC = () => {
  const {
    isAuthenticated,
    isAuthenticating,
    authenticationError,
    login,
  } = useAuth();
  const [state, setState] = useState<LoginState>({});
  const { email, password } = state;
  const navigate = useNavigate();

  const handleSubmit = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    login?.(email, password);
  };

  useEffect(() => {
    if (isAuthenticated && loggedUser.email != "") {
      navigate("/dashboard");
    }
  }, [isAuthenticated]);

  return (
    <div className="hero is-fullheight has-background-dark">
      <div />
      <div className="columns has-text-centered is-centered mt-6">
        <div className="column is-one-third"></div>
        <div className="column is-one-fifth mt-6">
          <div className="mt-6">
            <h1 className="title has-text-white is-size-1 pb-5">Log in</h1>
            <h2 className="subtitle has-text-white is-size-5">
              Start choosing your next favorite movie!
            </h2>
            <form onSubmit={handleSubmit} className="form">
              <div className="field py-2">
                <input
                  type="email"
                  id = "email"
                  placeholder="Username"
                  className="input has-background-info border-info has-text-white"
                  onChange={(e) =>
                    setState({ ...state, email: e.currentTarget.value || "" })
                  }
                  required
                />
              </div>
              <div className="field py-2">
                <input
                  type="password"
                  id = "password"
                  placeholder="Password"
                  className="input has-background-info border-info has-text-white"
                  onChange={(e) =>
                    setState({
                      ...state,
                      password: e.currentTarget.value || "",
                    })
                  }
                  required
                />
              </div>
              <button className="button is-primary is-fullwidth" id = "login">Log in</button>
              <div className="py-2">
                {authenticationError && (
                  <div className="info has-text-white mt-2">
                    {authenticationError.message} <br />
                    Please retry
                  </div>
                )}
              </div>
              {isAuthenticating && (
                <div className="columns is-fullwidth is-centered">
                  <div className="column is-one-third" />
                  <div className="column is-one-fifth mt-2">
                    <div className="control is-loading"></div>
                  </div>
                  <div className="column is-one-third" />
                </div>
              )}
            </form>
          </div>
        </div>
        <div className="column is-one-third"></div>
      </div>
      <div className="hero-foot">
        <figure className="image is-fullwidth">
          <img src={LoginFoot} alt="LoginFoot" className="" />
        </figure>
      </div>
    </div>
  );
};

export default Login;
