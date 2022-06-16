import { projectBaseUrl } from "./generalConstants";

export const locationMainBaseUrl: string = `${projectBaseUrl}${"dashboard/"}`;
export const locationBaseUrl: string = `${projectBaseUrl}${"locations/"}`;

export namespace LocationMainService {
  export const GET_TOP: string = `${locationMainBaseUrl}${"get-top/"}`;
  export const GET_ALL: string = `${locationMainBaseUrl}${"get-all/"}`;
}

export namespace LocationService {
  export const GET_ALL_BY_USER_ID: string = `${locationBaseUrl}${"get-all-by-user/"}`;
  export const DELETE_BY_ID: string = `${locationBaseUrl}${"delete/"}`;
  export const CREATE: string = `${locationBaseUrl}${"create/"}`;
  export const UPDATE: string = `${locationBaseUrl}${"update/"}`;
}

export namespace LoginService{
  export const GET_LOGGED_USER: string = `${projectBaseUrl}${"get-user/"}`;
}
