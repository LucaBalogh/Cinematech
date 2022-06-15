import { User } from "../model/IUser";
import {Location} from "../model/ILocation";
import axios from "axios";
import { config } from "../core";
import {LocationDetailsListItem} from "../model/ILocationDetailsListItem";

export const getByRequestUrl = (requestUrl: string) => {
  return axios
  .get(requestUrl, config)
  .then((res) => {
    return Promise.resolve(res.data);
  })
  .catch((err) => {
    console.log(err);
    return Promise.reject(err);
  });
};

export const getViewportAsPixels = (pageSizePx: number, viewportSize: number): number => {
    return (viewportSize * pageSizePx) / 100;
};

export const getDefaultLocation = (): Location => {
  return {
    id: 0,
    city: "",
    country: "",
    tip: "",
    rating: 0,
    user: getDefaultUser()
  };
};

export const getDefaultUser = (): User => {
  return {
    id: 0,
    firstName: "",
    lastName: "",
    email: "",
    password: ""
  };
};

export const getDefaultLocationDetailsListItem = (): LocationDetailsListItem => {
  return {
    id: 0,
    city: "",
    country: "",
    tip: "",
    rating: 0,
    user: ""
  };
};

export let loggedUser: User = getDefaultUser();

export const setLoggedUser = (value: User) => {
  loggedUser = value;
};

export let selectedLocation: LocationDetailsListItem = getDefaultLocationDetailsListItem();

export const setSelectedLocation = (value: LocationDetailsListItem) => {
  selectedLocation = value;
};