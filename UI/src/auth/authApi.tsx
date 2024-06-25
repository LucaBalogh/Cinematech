import axios from "axios";
import {config} from "../core";
import {User} from "../model/IUser";
import {projectBaseUrl} from "../utils/generalConstants";
import {setLoggedUser} from "../utils/utilsMethods";

const authUrl = `${projectBaseUrl}login`;

export const loginApi: (
  email?: string,
  password?: string
) => Promise<User> = async (email, password) => {
    try {
        const res = await axios
            .post(authUrl, {email, password}, config);
        setLoggedUser(res.data);
        return await Promise.resolve(res.data);
    } catch (err) {
        return await Promise.reject(err);
    }
};
