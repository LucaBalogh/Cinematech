import React, {useCallback, useState} from "react";
import PropTypes from "prop-types";
import {MovieProps} from "./MovieProps";
import {createMovie, updateMovie} from "./movieApi";

type SaveMovieFn = (movie: MovieProps) => Promise<any>;

export interface MovieState {
  saving: boolean;
  savingError?: Error | null;
  saveMovie?: SaveMovieFn;
}

const initialState: MovieState = {
  saving: false,
};

export const MovieContext = React.createContext<MovieState>(
  initialState
);

interface MovieProviderProps {
  children: PropTypes.ReactNodeLike;
}

export const MovieProvider: React.FC<MovieProviderProps> = ({
  children,
}) => {
  const [state, setState] = useState<MovieState>(initialState);
  const { saving, savingError } = state;
  const saveMovie = useCallback<SaveMovieFn>(saveMovieCallback, []);
  const value = {
    saving,
    savingError,
    saveMovie,
  };

  return (
    <MovieContext.Provider value={value}>
      {children}
    </MovieContext.Provider>
  );

  async function saveMovieCallback(movie: MovieProps) {
    try {
      setState({...state, savingError: null, saving: true});
      const mov = {
        id: movie.id!,
        name: movie.name!,
        tip: movie.tip!,
        rating: movie.rating!,
        user: movie.user!,
      };
      const savedMovie = await (movie.id
          ? updateMovie(mov)
          : createMovie(mov));
      setState({...state, savingError: null, saving: false});
      return savedMovie;
    } catch (error: any) {
      setState({...state, savingError: error, saving: false});
    }
  }
};
