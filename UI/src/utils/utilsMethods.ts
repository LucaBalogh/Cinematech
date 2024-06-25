import {User} from "../model/IUser";
import {Movie} from "../model/IMovie";
import axios from "axios";
import {config} from "../core";
import {MovieDetailsListItem} from "../model/IMovieDetailsListItem";

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

export const getDefaultMovie = (): Movie => {
  return {
    id: 0,
    name: "",
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

export const getDefaultMovieDetailsListItem = (): MovieDetailsListItem => {
  return {
    id: 0,
    name: "",
    tip: "",
    rating: 0,
    user: ""
  };
};

export let loggedUser: User = getDefaultUser();

export const setLoggedUser = (value: User) => {
  loggedUser = value;
};

export let selectedMovie: MovieDetailsListItem = getDefaultMovieDetailsListItem();

export const setSelectedMovie = (value: MovieDetailsListItem) => {
  selectedMovie = value;
};