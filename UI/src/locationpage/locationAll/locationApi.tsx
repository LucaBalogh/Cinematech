import axios from "axios";
import { config } from "../../core";
import { Location } from "../../model/ILocation";
import { LocationService } from "../../utils/service";

export const createLocation: (location: Location) => Promise<Location> = (
    location
) => {
  return axios
    .post(LocationService.CREATE, location)
    .then((res) => {
      return Promise.resolve(res.data);
    })
    .catch((err) => {
      return Promise.reject(err);
    });
};

export const updateLocation: (location: Location) => Promise<Location> = (
    location
) => {
  return axios
    .put(`${LocationService.UPDATE}${location.id}`, location, config)
    .then((res) => {
      return Promise.resolve(res.data);
    })
    .catch((err) => {
      return Promise.reject(err);
    });
};
