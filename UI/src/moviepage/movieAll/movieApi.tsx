import axios from "axios";
import {config} from "../../core";
import {Movie} from "../../model/IMovie";
import {MovieService} from "../../utils/service";

export const createMovie: (movie: Movie) => Promise<Movie> = (
    movie
) => {
  return axios
    .post(MovieService.CREATE, movie)
    .then((res) => {
      return Promise.resolve(res.data);
    })
    .catch((err) => {
      return Promise.reject(err);
    });
};

export const updateMovie: (movie: Movie) => Promise<Movie> = (
    movie
) => {
  return axios
    .put(`${MovieService.UPDATE}`, movie, config)
    .then((res) => {
      return Promise.resolve(res.data);
    })
    .catch((err) => {
      return Promise.reject(err);
    });
};
