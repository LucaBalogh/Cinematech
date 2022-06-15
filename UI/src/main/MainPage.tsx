import { CommandBar, DetailsList, IColumn, IContextualMenuItem, Stack, StackItem, ThemeProvider } from "@fluentui/react";
import { DetailsListLayoutMode, IObjectWithKey, Selection, SelectionMode } from "@fluentui/react/lib/DetailsList";
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../auth/AuthProvider";
import { IMainPageProps } from "../model/IMainPageProps";
import { LocationDetailsListItem } from "../model/ILocationDetailsListItem";
import {VIEW_LOCATIONS, LOGOUT} from "../utils/generalConstants";
import { LocationMainService } from "../utils/service";
import {
  getViewportAsPixels,
  getByRequestUrl,
  loggedUser, setSelectedLocation, selectedLocation, setLoggedUser, getDefaultUser
} from "../utils/utilsMethods";
import {
  commandBarStyles,
  defaultMenuItemStyle,
  detailsListColumnStyle,
  itemStyle,
  enabledMenuItemStyle,
  setGapBetweenHeaders,
  setGapBetweenHeadersAndDetailsList,
  transparentTheme,
  itemStyleForLastColumn,
  setGapBetweenHeadersAndUserInfo, setStyleForUserName
} from "./MainPage.styles";
import {Location} from "../model/ILocation";

const CITY_COLUMN: string = "City";
const COUNTRY_COLUMN: string = "Country";
const TIP_COLUMN: string = "Tip";
const RATING_COLUMN: string = "Rating";
const CREATED_BY_COLUMN: string = "User";

const getColumnName = (city: string, country: string, tip: string, rating: string, user: string, name: string): string => {
  return name === city
    ? city
    : name === country
    ? country
    : name === tip
    ? tip
    : name === rating
    ? rating
    : name === user
    ? user: name;
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
    : columnName === CREATED_BY_COLUMN
    ? "user"
    : "";
};

const getColumn = (pageWidth: number, name: string): IColumn => {
  return {
    key: name,
    name: getColumnName(CITY_COLUMN,COUNTRY_COLUMN,TIP_COLUMN,RATING_COLUMN,CREATED_BY_COLUMN,name),
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

export const getListItemFromMainPage = (locationMain: Location): LocationDetailsListItem => {
  return {
    id: locationMain.id,
    city: locationMain.city,
    country: locationMain.country,
    tip: locationMain.tip,
    rating: locationMain.rating,
    user: `${locationMain.user.firstName}${" "}${locationMain.user.lastName}`,
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

const getLocationMain = (allLocations: Location[]): LocationDetailsListItem[] => {
  return allLocations.map((item) => getListItemFromMainPage(item));
};

const getMenuItem = (name: string): IContextualMenuItem => {
  return {
    key: name,
    text: name,
    iconProps: { iconName: name },
  };
};

const getMenuItems = (names: string[]): IContextualMenuItem[] => {
  return names.map((name: string) => getMenuItem(name));
};

const MainPage = (props: IMainPageProps): JSX.Element => {
  const { isAuthenticated } = useAuth();
  const navigate = useNavigate();
  const [items, setItems] = useState<LocationDetailsListItem[]>([]);
  const [locationMain, setLocationMain] = useState<Location[]>([]);
  const [selectedItems, setSelectedItems] = useState<IObjectWithKey[] | undefined>(undefined);
  const [selection] = useState<Selection>(
    () =>
      new Selection({
        onSelectionChanged: () => {
          const selectedItems: IObjectWithKey[] = selection.getSelection();
          const selected: LocationDetailsListItem = selectedItems[0] as LocationDetailsListItem;
          setSelectedLocation(selected);
          setSelectedItems(selectedItems);
        }
      })
  );

  const columns: IColumn[] = getColumns(props.pageWidth, [CITY_COLUMN, COUNTRY_COLUMN, TIP_COLUMN, RATING_COLUMN, CREATED_BY_COLUMN]);
  const menuItems: IContextualMenuItem[] = getMenuItems([VIEW_LOCATIONS, LOGOUT]);

  useEffect(() => {
    if (!isAuthenticated) {
      navigate("/login");
    }
  }, [isAuthenticated]);

  useEffect(() => {
    getAllLocationsMain();
  }, []);


    const getAllLocationsMain = async () => {
      const allLocations: Location[] = await getByRequestUrl(`${LocationMainService.GET_TOP}${loggedUser.id}`);
      setItems(getLocationMain(allLocations));
      setLocationMain(allLocations);
    };

  const getSelectedItem = (): IObjectWithKey => {
    return selectedItems![0];
  };

  const getTitle = (): string => {
    return `${"Welcome back, "}${loggedUser.firstName}${ " "}${loggedUser.lastName}${"!"}`;
  };


  const onViewClicked = (): void => {
    if (isAuthenticated) {
      navigate("/locations");
    }
  };

  const onLogOutClicked = (): void => {
      setLoggedUser(getDefaultUser());
      navigate("/");
  };


  const updateMenuItems = (): IContextualMenuItem[] => {
    return menuItems.map((item: IContextualMenuItem) => {
      switch (item.key) {
        case VIEW_LOCATIONS:
          item.onClick = () => onViewClicked();
          item.style = enabledMenuItemStyle
          break;
        case LOGOUT:
          item.onClick = () => onLogOutClicked();
          item.style = enabledMenuItemStyle
          break;
        default:
          return item;
      }
      return item;
    });
  };

  return (
      <div>
        <Stack className="hero is-fullheight has-background-dark" tokens={setGapBetweenHeadersAndDetailsList}>
          <Stack horizontal tokens={setGapBetweenHeadersAndUserInfo}>
            <StackItem tokens={setGapBetweenHeaders}>
              <p className="title has-text-white is-size-3 marginFH2"> {getTitle()} </p>
            </StackItem>
          </Stack>
          <StackItem>
            <ThemeProvider theme={transparentTheme}>
              <CommandBar id = "view" items={updateMenuItems()} styles={commandBarStyles} />
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

export default MainPage;
