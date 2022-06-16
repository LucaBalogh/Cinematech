import { CommandBar, DetailsList, IColumn, IContextualMenuItem, Stack, StackItem, ThemeProvider } from "@fluentui/react";
import { DetailsListLayoutMode, IObjectWithKey, Selection, SelectionMode } from '@fluentui/react/lib/DetailsList';
import React, {useCallback, useEffect, useState} from "react";
import { useNavigate} from "react-router-dom";
import { useAuth } from "../auth/AuthProvider";
import { LocationDetailsListItem } from "../model/ILocationDetailsListItem";
import { LocationService } from "../utils/service";
import {
    getByRequestUrl,
    getViewportAsPixels,
    loggedUser,
    setSelectedLocation,
    selectedLocation,
    setLoggedUser, getDefaultUser
} from "../utils/utilsMethods";
import {commandBarStyles, defaultMenuItemStyle, detailsListColumnStyle, enabledMenuItemStyle, itemStyle, itemStyleForLastColumn, setGapBetweenHeaders, setGapBetweenHeadersAndDetailsList, setGapBetweenHeadersAndUserInfo, setStyleForUserName, transparentTheme} from "./Locations.styles";
import {ILocationProps} from "../model/ILocationProps";
import {Location} from "../model/ILocation";
import {ADD, DELETE, EDIT, VIEW_RECOMMENDATIONS, LOGOUT} from "../utils/generalConstants";
import SaveLocationModal from "./locationAll/SaveLocationModal";
import EditUserStoryModal from "./locationAll/EditLocationModal";

const CITY_COLUMN: string = "City";
const COUNTRY_COLUMN: string = "Country";
const TIP_COLUMN: string = "Tip";
const RATING_COLUMN: string = "Rating";

const getColumnName = (city: string, country: string, tip: string, rating: string, name: string): string => {
    return name === city
        ? city
        : name === country
            ? country
            : name === tip
                ? tip
                : name === rating
                    ? rating
                    : name;
};

const getFieldName = (columnName: string): string => {
    return columnName === CITY_COLUMN
        ? "city"
        : columnName === COUNTRY_COLUMN
            ? "country"
            : columnName === TIP_COLUMN
                ? "tip"
                : columnName === RATING_COLUMN
                    ? "rating"
                        : "";
};

const getColumn = (pageWidth: number, name: string): IColumn => {
    return {
        key: name,
        name: getColumnName(CITY_COLUMN,COUNTRY_COLUMN,TIP_COLUMN,RATING_COLUMN,name),
        fieldName: getFieldName(name),
        minWidth: getViewportAsPixels(pageWidth, 10),
        maxWidth: getViewportAsPixels(pageWidth, 20),
        isResizable: true,
        isMultiline: true,
        styles: detailsListColumnStyle,
    };
};

const getColumns = (pageWidth: number, names: string[]): IColumn[] => {
    return names.map((name: string) => getColumn(pageWidth, name));
};

export const getListItemFromLocation = (location : Location): LocationDetailsListItem => {
    return {
        id: location.id,
        city: location.city,
        country: location.country,
        tip: location.tip,
        rating: location.rating,
        user: `${location.user.firstName}${" "}${location.user.lastName}`,
    };
};

const renderItemColumn = (item: any, index?: number, column?: IColumn): React.ReactFragment => {
    const fieldContent = item[column!.fieldName as keyof LocationDetailsListItem] as string;
    
    return (
        <React.Fragment>
      {column!.fieldName !== "created"
        ? <span className={itemStyle}>{fieldContent}</span>
        : <span className={itemStyleForLastColumn}>{fieldContent}</span>
      }
    </React.Fragment>
    );
};

const getLocationForCurrentUser = (allLocations: Location[]): LocationDetailsListItem[] => {
    return allLocations.map((item) => getListItemFromLocation(item) );
};

const getMenuItem = (name: string): IContextualMenuItem => {
    return {
        key: name,
        text: name,
        iconProps: { iconName: name }
    }
  };
  
  const getMenuItems = (names: string[]): IContextualMenuItem[] => {
    return names.map((name: string) => getMenuItem(name));
  };

const Locations = (props: ILocationProps): JSX.Element => {
    const { isAuthenticated } = useAuth();
    const navigate = useNavigate();
    const [deleteItemId, setDeleteItemId] = useState<number>(0);
    const [items, setItems] = useState<LocationDetailsListItem[]>([]);
    const [selectedItems, setSelectedItems] = useState<IObjectWithKey[] | undefined>(undefined);
    const [locations, setLocations] = useState<Location[]>([]);
    const [selection] = useState<Selection>(() => new Selection({
        onSelectionChanged: () => {
            const selectedItems: IObjectWithKey[] = selection.getSelection();
            const selected: LocationDetailsListItem = selectedItems[0] as LocationDetailsListItem;
            setSelectedLocation(selected);
            setSelectedItems(selectedItems);
        }
    }));
    const columns: IColumn[] = getColumns(props.pageWidth, [CITY_COLUMN, COUNTRY_COLUMN, TIP_COLUMN, RATING_COLUMN]);
    const menuItems: IContextualMenuItem[] = getMenuItems([ADD, EDIT, DELETE, VIEW_RECOMMENDATIONS, LOGOUT]);

    useEffect(() => {
        if (!isAuthenticated) {
            navigate("/login");
        }
    }, [isAuthenticated]);

    useEffect(() => {
        getAllLocationsForCurrentUser();
    }, []);

    const [isSaving, setIsSaving] = useState(false);
    const switchSavingMode = useCallback(
        () => setIsSaving((isSaving) => !isSaving),
        []
    );
    const [isEditing, setIsEditing] = useState(false);
    const switchEditingMode = useCallback(
        () => setIsEditing((isEditing) => !isEditing),
        []
    );

    useEffect(() => {
        if(deleteItemId === 0) {
          return;
        }
    
        deleteLocation();
      }, [deleteItemId]);

    const getAllLocationsForCurrentUser = async () => {
        const allLocations: Location[] = await getByRequestUrl(`${LocationService.GET_ALL_BY_USER_ID}${loggedUser.id}`);
        setItems(getLocationForCurrentUser(allLocations));
        setLocations(allLocations)
    };

    const getSelectedLocation = (): Location => {
        const index = locations.findIndex((it) => it.id === selectedLocation.id);
        return locations[index];
    };

    const deleteLocation = async () => {
        const location: LocationDetailsListItem = getSelectedItem() as LocationDetailsListItem;
        const requestUrl: string = `${LocationService.DELETE_BY_ID}${location.id}`;
        const message: string = await getByRequestUrl(requestUrl);
    
        if(message === "Success") {
            getAllLocationsForCurrentUser();
        }
        else {
          alert("An error has occurred on delete operation");
        }
    };

    const getSelectedItem = (): IObjectWithKey => {
        return selectedItems![0];
    };

    const getTitle = (): string => {
        return `${loggedUser.firstName}${" "}${loggedUser.lastName}${"'s locations"}`;
    };

    const isEditOrDeleteDisabled = (checkEdit: boolean): boolean => {
        if (!selectedItems)
            return true;
    
        if (checkEdit) {
            if (selectedItems.length !== 1)
                return true;
        }
        else
            if (selectedItems.length < 1)
                return true;
        return false;
      };

    const onViewClicked = (): void => {
        if (isAuthenticated) {
            navigate("/dashboard");
        }
    };

    const onLogOutClicked = (): void => {
        setLoggedUser(getDefaultUser());
        navigate("/");
    };

    const onAddClicked = (): void => {
        switchSavingMode();
    };

    const onEditClicked = (): void => {
        if (locations.find((us) => us.id === selectedLocation.id) !== undefined) {
            switchEditingMode();
        }
    };

    const onDeleteClicked = (): void => {
        const deleteLoc: LocationDetailsListItem = getSelectedItem() as LocationDetailsListItem;
        setDeleteItemId(deleteLoc.id);
    };

    const updateMenuItems = (): IContextualMenuItem[] => {
        return menuItems.map((item: IContextualMenuItem) => {
            switch (item.key) {
                case ADD:
                    item.onClick = () => onAddClicked();
                    item.style = enabledMenuItemStyle;
                    break;
                case VIEW_RECOMMENDATIONS:
                    item.onClick = () => onViewClicked();
                    item.style = enabledMenuItemStyle
                    break;
                case LOGOUT:
                    item.onClick = () => onLogOutClicked();
                    item.style = enabledMenuItemStyle
                    break;
                case EDIT:
                    item.disabled = isEditOrDeleteDisabled(true);
                    item.onClick = () => onEditClicked();
                    item.style =
                        selectedItems?.length === 1
                            ? enabledMenuItemStyle
                            : defaultMenuItemStyle;
                    break;
                case DELETE:
                    item.disabled = isEditOrDeleteDisabled(false);
                    item.onClick = () => onDeleteClicked();
                    item.style =
                        selectedItems?.length === 1
                            ? enabledMenuItemStyle
                            : defaultMenuItemStyle;
                    break;
                default:
                    return item;
            }
            return item;
        });
    };

    return (
        <div>
        {isSaving && (
            <SaveLocationModal
                switchMode={switchSavingMode}
                items={items}
                setItems={setItems}
                locations = {locations}
                setLocations = {setLocations}
            />
        )}
        {isEditing && (
            <EditUserStoryModal
                switchMode={switchEditingMode}
                location={getSelectedLocation()}
                items={items}
                setItems={setItems}
                locations={locations}
                setLocations={setLocations}
            />
         )}
        <Stack className="hero is-fullheight has-background-dark" tokens={setGapBetweenHeadersAndDetailsList}>
            <Stack horizontal tokens={setGapBetweenHeadersAndUserInfo}>
                <StackItem tokens={setGapBetweenHeaders}>
                    <p className="title has-text-white is-size-3 marginFH2"> {getTitle()} </p>
                </StackItem>
            </Stack>
            <StackItem>
                <ThemeProvider theme={transparentTheme}>
                    <CommandBar items={updateMenuItems()} styles={commandBarStyles} />
                    <DetailsList className="hero is-fullheight has-background-dark"
                        items={items}
                        setKey="set"
                        columns={columns}
                        selectionMode={SelectionMode.single}
                        layoutMode={DetailsListLayoutMode.justified}
                        selection={selection}
                        selectionPreservedOnEmptyClick={true}
                        onRenderItemColumn={renderItemColumn}>
                    </DetailsList>
                </ThemeProvider>
            </StackItem>
        </Stack>
        </div>
    );
};

export default Locations;
