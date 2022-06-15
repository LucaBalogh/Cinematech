import React, {
  createContext,
  useCallback,
  useContext,
  useEffect,
  useState,
} from "react";
import PropTypes from "prop-types";
import { loginApi } from "./authApi";
import { User } from "../model/IUser";

type LoginFn = (email?: string, password?: string) => void;

const initialState: AuthState = {
  isAuthenticated: false,
  isAuthenticating: false,
  authenticationError: null,
  pendingAuthentication: false,
};

export interface AuthState {
  authenticationError: Error | null;
  isAuthenticated: boolean;
  isAuthenticating: boolean;
  pendingAuthentication?: boolean;
  email?: string;
  password?: string;
  user?: User;
  login?: LoginFn;
}

export const AuthContext = createContext(initialState);

interface AuthProviderProps {
  children: PropTypes.ReactNodeLike;
}

export const useAuth = () => {
  return useContext(AuthContext);
};

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [state, setState] = useState<AuthState>(initialState);
  const {
    isAuthenticated,
    isAuthenticating,
    authenticationError,
    pendingAuthentication,
    user,
  } = state;
  const login = useCallback<LoginFn>(loginCallback, [state]);
  useEffect(authenticationEffect, [pendingAuthentication]);
  const value = {
    isAuthenticated,
    isAuthenticating,
    authenticationError,
    user,
    login,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;

  function loginCallback(email?: string, password?: string): void {
    setState({
      ...state,
      pendingAuthentication: true,
      email,
      password,
    });
  }

  function authenticationEffect() {
    let canceled = false;
    authenticate();
    return () => {
      canceled = true;
    };

    async function authenticate() {
      if (!pendingAuthentication) {
        return;
      }
      try {
        setState({
          ...state,
          isAuthenticating: true,
        });
        const { email, password } = state;
        const user = await loginApi(email, password);
        if (canceled) {
          return;
        }
        setState({
          ...state,
          pendingAuthentication: false,
          isAuthenticated: true,
          isAuthenticating: false,
          user,
        });
      } catch (error: any) {
        if (canceled) {
          return;
        }
        setState({
          ...state,
          authenticationError: error,
          pendingAuthentication: false,
          isAuthenticating: false,
        });
      }
    }
  }
};
