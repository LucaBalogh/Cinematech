import React, {FormEvent, useContext, useState} from "react";
import {AuthContext} from "../../auth/AuthProvider";
import {Movie} from "../../model/IMovie";
import {MovieDetailsListItem} from "../../model/IMovieDetailsListItem";
import {MovieContext} from "./MovieProvider";
import {getListItemFromMovie} from "../Movies";
import {User} from "../../model/IUser";

export interface SaveMovieModalProps {
  switchMode: () => void;
  items: MovieDetailsListItem[];
  setItems: (items: MovieDetailsListItem[]) => void;
  movies: Movie[];
  setMovies: (items: Movie[]) => void;
}

interface SaveMovieModalState {
  name?: string;
  tip?: string;
  rating?: number;
  user?: User;
}

const SaveMovieModal: React.FC<SaveMovieModalProps> = ({
  switchMode,
  items,
  setItems,
  movies,
  setMovies,
}) => {
  const { saving, savingError, saveMovie } = useContext(MovieContext);
  const { user } = useContext(AuthContext);
  const initialState = {
    user: user,
  };
  const [state, setState] = useState<SaveMovieModalState>(initialState);

  const handleSubmit = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    saveMovie?.(state).then((mov: Movie) => {
      console.log(mov);
      if (mov) {
        const newItems: MovieDetailsListItem[] = items;
        newItems.splice(items.length, 0, getListItemFromMovie(mov));
        movies.splice(movies.length, 0, mov);
        setItems(newItems);
        setMovies(movies);
        switchMode();
      }
    });
  };

  return (
    <div className="modal is-active">
      <div
        className="modal-background"
        onClick={() => {
          switchMode();
        }}
      />
      <div className="modal-card">
        <section className="modal-card-body has-background-light p-6">
          <div className="content">
            <p className="modal-card-title">Add Movie</p>
            <form onSubmit={handleSubmit} className="form">
              <div className="field mt-4">
                <div className="label has-text-weight-light">Name</div>
                <input
                  type="text"
                  id = "name"
                  className="input"
                  onChange={(e) =>
                    setState({ ...state, name: e.currentTarget.value || "" })
                  }
                  required
                />
              </div>
              <div className="field mt-4">
                <div className="label has-text-weight-light">Tip</div>
                <input
                    type="text"
                    id = "tip"
                    className="input"
                    onChange={(e) =>
                        setState({ ...state, tip: e.currentTarget.value || "" })
                    }
                    required
                />
              </div>
              <div className="field mt-4">
                <div className="label has-text-weight-light">Rating</div>
                <input
                    type="text"
                    id = "rating"
                    className="input"
                    onChange={(e) =>
                        setState({ ...state, rating: parseFloat(e.currentTarget.value) || 0, })
                    }
                    required
                />
              </div>
              <div className="py-2">
                {savingError && (
                  <div className="info has-text-error is-size-7 mt-2">
                    {savingError.message} <br />
                    Please use correct values
                  </div>
                )}
              </div>
              {!saving && (
                <button className="button is-dark is-fullwidth" id = "save">Save</button>
              )}
              {saving && (
                <div className="is-dark is-fullwidth mt-6">
                  <div className="columns is-fullwidth is-centered">
                    <div className="column is-one-third" />
                    <div className="column is-one-fifth mt-2">
                      <div className="control is-loading"></div>
                    </div>
                    <div className="column is-one-third" />
                  </div>
                </div>
              )}
              <button
                className="button is-fullwidth mt-5"
                onClick={() => {
                  switchMode();
                }}
              >
                Cancel
              </button>
            </form>
          </div>
        </section>
      </div>
    </div>
  );
};

export default SaveMovieModal;
