import {projectBaseUrl} from "./generalConstants";

export const movieMainBaseUrl: string = `${projectBaseUrl}${"dashboard/"}`;
export const movieBaseUrl: string = `${projectBaseUrl}${"movies/"}`;

export namespace MovieMainService {
  export const GET_TOP: string = `${movieMainBaseUrl}${"get-top/"}`;
  export const GET_ALL: string = `${movieMainBaseUrl}${"get-all/"}`;
}

export namespace MovieService {
  export const GET_ALL_BY_USER_ID: string = `${movieBaseUrl}${"get-all-by-user/"}`;
  export const DELETE_BY_ID: string = `${movieBaseUrl}${"delete/"}`;
  export const CREATE: string = `${movieBaseUrl}${"create/"}`;
  export const UPDATE: string = `${movieBaseUrl}${"update/"}`;
  export const SEARCH_NAME: string = `${movieBaseUrl}${"search/"}`;
}

export namespace LoginService{
  export const GET_LOGGED_USER: string = `${projectBaseUrl}${"get-user/"}`;
}
