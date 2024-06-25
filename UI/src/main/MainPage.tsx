import {CommandBar, DetailsList, IColumn, IContextualMenuItem, Stack, StackItem, ThemeProvider} from "@fluentui/react";
import {DetailsListLayoutMode, IObjectWithKey, Selection, SelectionMode} from "@fluentui/react/lib/DetailsList";
import React, {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {useAuth} from "../auth/AuthProvider";
import {IMainPageProps} from "../model/IMainPageProps";
import {MovieDetailsListItem} from "../model/IMovieDetailsListItem";
import {LOGOUT, VIEW_MOVIES} from "../utils/generalConstants";
import {MovieMainService} from "../utils/service";
import {
    getByRequestUrl,
    getDefaultUser,
    getViewportAsPixels,
    loggedUser,
    setLoggedUser,
    setSelectedMovie
} from "../utils/utilsMethods";
import {
    commandBarStyles,
    detailsListColumnStyle,
    enabledMenuItemStyle,
    itemStyle,
    itemStyleForLastColumn,
    setGapBetweenHeaders,
    setGapBetweenHeadersAndDetailsList,
    setGapBetweenHeadersAndUserInfo,
    transparentTheme
} from "./MainPage.styles";
import {Movie} from "../model/IMovie";

const NAME_COLUMN: string = "Name";
const TIP_COLUMN: string = "Tip";
const RATING_COLUMN: string = "Rating";
const CREATED_BY_COLUMN: string = "User";

const getColumnName = (movieName: string, tip: string, rating: string, user: string, name: string): string => {
  return name === movieName
    ? movieName
    : name === tip
    ? tip
    : name === rating
    ? rating
    : name === user
    ? user: name;
};

const getFieldName = (columnName: string): string => {
  return columnName === NAME_COLUMN
    ? "name"
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
    name: getColumnName(NAME_COLUMN,TIP_COLUMN,RATING_COLUMN,CREATED_BY_COLUMN,name),
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

export const getListItemFromMainPage = (movieMain: Movie): MovieDetailsListItem => {
  return {
    id: movieMain.id,
    name: movieMain.name,
    tip: movieMain.tip,
    rating: movieMain.rating,
    user: `${movieMain.user.firstName}${" "}${movieMain.user.lastName}`,
  };
};

const renderItemColumn = (item: any, index?: number, column?: IColumn): React.ReactFragment => {
  const fieldContent = item[column!.fieldName as keyof MovieDetailsListItem] as string;

  return (
    <React.Fragment>
      {column!.fieldName !== "created"
        ? <span className={itemStyle}>{fieldContent}</span>
        : <span className={itemStyleForLastColumn}>{fieldContent}</span>
      }
    </React.Fragment>
  );
};

const getMovieMain = (allMovies: Movie[]): MovieDetailsListItem[] => {
  return allMovies.map((item) => getListItemFromMainPage(item));
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
  const [items, setItems] = useState<MovieDetailsListItem[]>([]);
  const [movieMain, setMovieMain] = useState<Movie[]>([]);
  const [selectedItems, setSelectedItems] = useState<IObjectWithKey[] | undefined>(undefined);
  const [selection] = useState<Selection>(
    () =>
      new Selection({
        onSelectionChanged: () => {
          const selectedItems: IObjectWithKey[] = selection.getSelection();
          const selected: MovieDetailsListItem = selectedItems[0] as MovieDetailsListItem;
          setSelectedMovie(selected);
          setSelectedItems(selectedItems);
        }
      })
  );

  const columns: IColumn[] = getColumns(props.pageWidth, [NAME_COLUMN, TIP_COLUMN, RATING_COLUMN, CREATED_BY_COLUMN]);
  const menuItems: IContextualMenuItem[] = getMenuItems([VIEW_MOVIES, LOGOUT]);

  useEffect(() => {
    if (!isAuthenticated) {
      navigate("/login");
    }
  }, [isAuthenticated]);

  useEffect(() => {
    getAllMoviesMain();
  }, []);


    const getAllMoviesMain = async () => {
      const allMovies: Movie[] = await getByRequestUrl(`${MovieMainService.GET_TOP}${loggedUser.id}`);
      setItems(getMovieMain(allMovies));
      setMovieMain(allMovies);
    };

  const getSelectedItem = (): IObjectWithKey => {
    return selectedItems![0];
  };

  const getTitle = (): string => {
    return `${"Welcome back, "}${loggedUser.firstName}${ " "}${loggedUser.lastName}${"!"}`;
  };


  const onViewClicked = (): void => {
    if (isAuthenticated) {
      navigate("/movies");
    }
  };

  const onLogOutClicked = (): void => {
      setLoggedUser(getDefaultUser());
      navigate("/");
  };


  const updateMenuItems = (): IContextualMenuItem[] => {
    return menuItems.map((item: IContextualMenuItem) => {
      switch (item.key) {
        case VIEW_MOVIES:
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
