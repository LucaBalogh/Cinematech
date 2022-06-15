import axios from "axios";
import { config } from "../core";
import { User } from "../model/IUser";
import { projectBaseUrl } from "../utils/generalConstants";
import { setLoggedUser } from "../utils/utilsMethods";

const authUrl = `${projectBaseUrl}login`;

export const loginApi: (
  email?: string,
  password?: string
) => Promise<User> = (email, password) => {
  return axios
    .post(authUrl, { email, password }, config)
    .then((res) => {
      setLoggedUser(res.data);
      return Promise.resolve(res.data);
    })
    .catch((err) => {
      return Promise.reject(err);
    });
};
