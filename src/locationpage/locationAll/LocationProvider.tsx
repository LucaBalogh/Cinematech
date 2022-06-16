import React, { useCallback, useState } from "react";
import PropTypes from "prop-types";
import { LocationProps } from "./LocationProps";
import { createLocation, updateLocation } from "./locationApi";

type SaveLocationFn = (location: LocationProps) => Promise<any>;

export interface LocationState {
  saving: boolean;
  savingError?: Error | null;
  saveLocation?: SaveLocationFn;
}

const initialState: LocationState = {
  saving: false,
};

export const LocationContext = React.createContext<LocationState>(
  initialState
);

interface LocationProviderProps {
  children: PropTypes.ReactNodeLike;
}

export const LocationProvider: React.FC<LocationProviderProps> = ({
  children,
}) => {
  const [state, setState] = useState<LocationState>(initialState);
  const { saving, savingError } = state;
  const saveLocation = useCallback<SaveLocationFn>(saveLocationCallback, []);
  const value = {
    saving,
    savingError,
    saveLocation,
  };

  return (
    <LocationContext.Provider value={value}>
      {children}
    </LocationContext.Provider>
  );

  async function saveLocationCallback(location: LocationProps) {
    try {
      setState({...state, savingError: null, saving: true});
      const loc = {
        id: location.id!,
        city: location.city!,
        country: location.country!,
        tip: location.tip!,
        rating: location.rating!,
        user: location.user!,
      };
      const savedLocation = await (location.id
          ? updateLocation(loc)
          : createLocation(loc));
      setState({...state, savingError: null, saving: false});
      return savedLocation;
    } catch (error: any) {
      setState({...state, savingError: error, saving: false});
    }
  }
};
