import React, {FormEvent, useContext, useState} from "react";
import {User} from "../../model/IUser";
import {Movie} from "../../model/IMovie";
import {MovieDetailsListItem} from "../../model/IMovieDetailsListItem";
import {Tip} from "../../model/Tip";
import {MovieContext} from "./MovieProvider";
import {getListItemFromMovie} from "../Movies";

export interface EditMovieModalProps {
  switchMode: () => void;
  movie: Movie;
  items: MovieDetailsListItem[];
  setItems: (items: MovieDetailsListItem[]) => void;
  movies: Movie[];
  setMovies: (items: Movie[]) => void;
}

interface EditMovieModalState {
  id: number;
  name: string;
  tip: string;
  rating: number;
  user: User;
}

const CreateMovieModal: React.FC<EditMovieModalProps> = ({
  switchMode,
  movie,
  items,
  setItems,
  movies,
  setMovies,
}) => {
  const { saving, savingError, saveMovie } = useContext(MovieContext);
  const initialState = {...movie};
  const [state, setState] = useState<EditMovieModalState>(initialState);


  const handleSubmit = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    saveMovie?.(state).then((mov: Movie) => {
      console.log(mov);
      if (mov) {
        const index = items.findIndex((it) => it.id === movie.id);
        if (index !== -1) {
          items[index] = getListItemFromMovie(mov);
          movies[index] = mov;
        }
        setItems(items);
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
            <p className="modal-card-title">Edit Movie</p>
            <form onSubmit={handleSubmit} className="form">
              <div className="field mt-4">
                <div className="label has-text-weight-light">Name</div>
                <input
                  value={state.name}
                  type="text"
                  className="input"
                  id = "name"
                  onChange={(e) =>
                    setState({ ...state, name: e.currentTarget.value || "" })
                  }
                  required
                />
              </div>
              <div className="field is-expanded is-fullwidth mt-4">
                <label className="label has-text-weight-light">Tip</label>
                <div className="control is-expanded is-fullwidth">
                  <span className="select is-fullwidth">
                    <select
                        id = "selectTip"
                        className="has-text-weight-normal"
                        onChange={(e) => {
                          setState({
                            ...state,
                            tip: e.currentTarget.value,
                          });
                        }}
                    >
                      {Object.values(Tip)
                          .filter((item) => {
                            return isNaN(Number(item));
                          })
                          .map((item) =>
                              item === state.tip ? (
                                  <option key={item} selected id = {`${item}`}>
                                    {item}
                                  </option>
                              ) : (
                                  <option key={item} id = {`${item}`}>{item}</option>
                              )
                          )}
                    </select>
                  </span>
                </div>
              </div>
              <div className="field mt-4">
                <div className="label has-text-weight-light">Rating</div>
                <input
                    value={state.rating}
                    type="number"
                    step="0.1"
                    className="input"
                    id = "rating"
                    onChange={(e) =>
                        setState({ ...state, rating: parseFloat(e.currentTarget.value) || 0, })
                    }
                    required
                />
              </div>
              <div className="field mt-4">
                <div className="label has-text-weight-light">User</div>
                <input
                    value={`${state.user.firstName} ${state.user.lastName}`}
                    type="text"
                    className="input"
                    readOnly
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
                <button className="button is-dark is-fullwidth" id = "update">Edit</button>
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

export default CreateMovieModal;
