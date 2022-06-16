import React, { FormEvent, useContext, useState } from "react";
import { AuthContext } from "../../auth/AuthProvider";
import { Location } from "../../model/ILocation";
import { LocationDetailsListItem } from "../../model/ILocationDetailsListItem";
import { LocationContext } from "./LocationProvider";
import {getListItemFromLocation} from "../Locations";
import {User} from "../../model/IUser";

export interface SaveLocationModalProps {
  switchMode: () => void;
  items: LocationDetailsListItem[];
  setItems: (items: LocationDetailsListItem[]) => void;
  locations: Location[];
  setLocations: (items: Location[]) => void;
}

interface SaveLocationModalState {
  city?: string;
  country?: string;
  tip?: string;
  rating?: number;
  user?: User;
}

const SaveLocationModal: React.FC<SaveLocationModalProps> = ({
  switchMode,
  items,
  setItems,
  locations,
  setLocations,
}) => {
  const { saving, savingError, saveLocation } = useContext(LocationContext);
  const { user } = useContext(AuthContext);
  const initialState = {
    user: user,
  };
  const [state, setState] = useState<SaveLocationModalState>(initialState);

  const handleSubmit = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    saveLocation?.(state).then((loc: Location) => {
      console.log(loc);
      if (loc) {
        const newItems: LocationDetailsListItem[] = items;
        newItems.splice(items.length, 0, getListItemFromLocation(loc));
        locations.splice(locations.length, 0, loc);
        setItems(newItems);
        setLocations(locations);
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
            <p className="modal-card-title">Add Location</p>
            <form onSubmit={handleSubmit} className="form">
              <div className="field mt-4">
                <div className="label has-text-weight-light">City</div>
                <input
                  type="text"
                  id = "city"
                  className="input"
                  onChange={(e) =>
                    setState({ ...state, city: e.currentTarget.value || "" })
                  }
                  required
                />
              </div>
              <div className="field mt-4">
                <div className="label has-text-weight-light">Country</div>
                <input
                    type="text"
                    id = "country"
                    className="input"
                    onChange={(e) =>
                        setState({ ...state, country: e.currentTarget.value || "" })
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

export default SaveLocationModal;
