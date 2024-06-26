import {CommandBar,
    DetailsList,
    Dialog,
    DialogType,
    DialogFooter,
    IContextualMenuItem,
    IColumn,
    PrimaryButton,
    DefaultButton,
    Stack,
    StackItem,
    ThemeProvider} from "@fluentui/react";
import {DetailsListLayoutMode, IObjectWithKey, Selection, SelectionMode} from '@fluentui/react/lib/DetailsList';
import React, {useCallback, useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {useAuth} from "../auth/AuthProvider";
import {MovieDetailsListItem} from "../model/IMovieDetailsListItem";
import {MovieService} from "../utils/service";
import {
    getByRequestUrl,
    getDefaultUser,
    getViewportAsPixels,
    loggedUser,
    selectedMovie,
    setLoggedUser,
    setSelectedMovie
} from "../utils/utilsMethods";
import {
    commandBarStyles,
    defaultMenuItemStyle,
    detailsListColumnStyle,
    enabledMenuItemStyle,
    itemStyle,
    itemStyleForLastColumn,
    setGapBetweenHeaders,
    setGapBetweenHeadersAndDetailsList,
    setGapBetweenHeadersAndUserInfo,
    transparentTheme
} from "./Movies.styles";
import {IMovieProps} from "../model/IMovieProps";
import {Movie} from "../model/IMovie";
import {ADD, DELETE, EDIT, LOGOUT, SEARCH, VIEW_RECOMMENDATIONS} from "../utils/generalConstants";
import SaveMovieModal from "./movieAll/SaveMovieModal";
import EditUserStoryModal from "./movieAll/EditMovieModal";

const NAME_COLUMN: string = "Name";
const TIP_COLUMN: string = "Tip";
const RATING_COLUMN: string = "Rating";
type MovieColumnKey = keyof Pick<Movie, 'id' | 'name' | 'tip' | 'rating'>;

const getColumnName = (movieName: string, tip: string, rating: string, name: string): string => {
    return name === movieName
        ? movieName
            : name === tip
                ? tip
                : name === rating
                    ? rating
                    : name;
};

const getFieldName = (columnName: string): MovieColumnKey => {
    return columnName === NAME_COLUMN
        ? "name"
            : columnName === TIP_COLUMN
                ? "tip"
                : columnName === RATING_COLUMN
                    ? "rating"
                        : "" as MovieColumnKey;
};

const getColumn = (pageWidth: number,
                   name: string,
                   sortColumn: MovieColumnKey,
                   sortOrder: string,
                   onColumnClick: (columnKey: MovieColumnKey) => void
): IColumn => {
    const columnKey = getFieldName(name);
    return {
        key: name,
        name: getColumnName(NAME_COLUMN,TIP_COLUMN,RATING_COLUMN,name),
        fieldName: columnKey,
        minWidth: getViewportAsPixels(pageWidth, 10),
        maxWidth: getViewportAsPixels(pageWidth, 20),
        isResizable: true,
        isMultiline: true,
        styles: detailsListColumnStyle,
        isSorted: sortColumn === columnKey,
        isSortedDescending: sortOrder === "desc",
        onColumnClick: () => onColumnClick(columnKey)
    };
};

const getColumns = (pageWidth: number, names: string[], sortColumn: MovieColumnKey,
                    sortOrder: string,
                    onColumnClick: (columnKey: MovieColumnKey) => void): IColumn[] => {
    return names.map((name: string) => getColumn(pageWidth, name, sortColumn, sortOrder, onColumnClick));
};

export const getListItemFromMovie = (movie : Movie): MovieDetailsListItem => {
    return {
        id: movie.id,
        name: movie.name,
        tip: movie.tip,
        rating: movie.rating,
        user: `${movie.user.firstName}${" "}${movie.user.lastName}`,
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

const getMovieForCurrentUser = (allMovies: Movie[]): MovieDetailsListItem[] => {
    return allMovies.map((item) => getListItemFromMovie(item) );
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

const Movies = (props: IMovieProps): JSX.Element => {
    const { isAuthenticated } = useAuth();
    const navigate = useNavigate();
    const [deleteItemId, setDeleteItemId] = useState<number>(0);
    const [items, setItems] = useState<MovieDetailsListItem[]>([]);
    const [selectedItems, setSelectedItems] = useState<IObjectWithKey[] | undefined>(undefined);
    const [movies, setMovies] = useState<Movie[]>([]);
    const [selection] = useState<Selection>(() => new Selection({
        onSelectionChanged: () => {
            const selectedItems: IObjectWithKey[] = selection.getSelection();
            const selected: MovieDetailsListItem = selectedItems[0] as MovieDetailsListItem;
            setSelectedMovie(selected);
            setSelectedItems(selectedItems);
        }
    }));
    const [sortColumn, setSortColumn] = useState<MovieColumnKey>("id");
    const [sortOrder, setSortOrder] = useState<string>("asc");
    const [isDialogVisible, setIsDialogVisible] = useState(false);
    const [movieToDelete, setMovieToDelete] = useState<MovieDetailsListItem | null>(null);
    const menuItems: IContextualMenuItem[] = getMenuItems([VIEW_RECOMMENDATIONS, ADD, SEARCH, EDIT, DELETE, LOGOUT]);

    useEffect(() => {
        if (!isAuthenticated) {
            navigate("/login");
        } else {
            getAllMoviesForCurrentUser(); // Fetch movies when the component mounts
        }
    }, [isAuthenticated]);

    useEffect(() => {
        getAllMoviesForCurrentUser();
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

        deleteMovie();
      }, [deleteItemId]);

    const getAllMoviesForCurrentUser = async () => {
        try {
            const allMovies: Movie[] = await getByRequestUrl(`${MovieService.GET_ALL_BY_USER_ID}/${loggedUser.id}`);
            setItems(getMovieForCurrentUser(allMovies));
            setMovies(allMovies);
        } catch (error) {
            console.error('Error fetching movies:', error);
        }
    };

    const onColumnClick = (columnKey: MovieColumnKey) => {
        let newSortOrder = "asc";
        if (sortColumn === columnKey && sortOrder === "asc") {
            newSortOrder = "desc";
        }
        setSortColumn(columnKey);
        setSortOrder(newSortOrder);

        const sortedMovies = [...movies].sort((a, b) => {
            if (a[columnKey] < b[columnKey]) return newSortOrder === "asc" ? -1 : 1;
            if (a[columnKey] > b[columnKey]) return newSortOrder === "asc" ? 1 : -1;
            return 0;
        });

        setItems(getMovieForCurrentUser(sortedMovies));
        setMovies(sortedMovies);
    };

    const columns: IColumn[] = getColumns(props.pageWidth, [NAME_COLUMN, TIP_COLUMN, RATING_COLUMN], sortColumn,
        sortOrder,
        onColumnClick);

    const getSelectedMovie = (): Movie => {
        const index = movies.findIndex((it) => it.id === selectedMovie.id);
        return movies[index];
    };

    const deleteMovie = async () => {
        try {
            if (!movieToDelete) return;

            const requestUrl: string = `${MovieService.DELETE_BY_ID}/${movieToDelete.id}`;
            const message: string = await getByRequestUrl(requestUrl);

            if (message === "Success") {
                getAllMoviesForCurrentUser();
            } else {
                alert("An error has occurred on delete operation");
            }
            setMovieToDelete(null);
        } catch (error) {
            console.error('Error deleting movie:', error);
        }
    };

    const searchMovie = async (name: string) => {
        try {
            if(name === null || name.trim() === '')
                getAllMoviesForCurrentUser()
            else {
                const requestUrl: string = `${MovieService.SEARCH_NAME}/${loggedUser.id}/${name}`;
                const movieFound: Movie | null = await getByRequestUrl(requestUrl);
                if (movieFound !== null){
                    const updatedItems: MovieDetailsListItem[] = getMovieForCurrentUser([movieFound]);
                    setItems(updatedItems);
                    setMovies([movieFound]);
                }
            }
        } catch (error) {
            console.error('Error searching movies:', error);
        }
    };

    const getSelectedItem = (): IObjectWithKey => {
        return selectedItems![0];
    };

    const getTitle = (): string => {
        return `${loggedUser.firstName}${" "}${loggedUser.lastName}${"'s movies"}`;
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

        setTimeout(() => {
            getAllMoviesForCurrentUser();
        }, 7000);
    };

    const onEditClicked = (): void => {
        if (movies.find((us) => us.id === selectedMovie.id) !== undefined) {
            switchEditingMode();
            getAllMoviesForCurrentUser();
        }
    };

    const onDeleteClicked = (): void => {
        const deleteMov: MovieDetailsListItem = getSelectedItem() as MovieDetailsListItem;
        setMovieToDelete(deleteMov);
        setIsDialogVisible(true);
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
                case SEARCH:
                    item.onClick = () => {
                        const nameToSearch = prompt('Enter movie name to search: ');
                        if (nameToSearch) {
                            searchMovie(nameToSearch);
                        }
                    };
                    item.style = enabledMenuItemStyle;
                    break;
                default:
                    return item;
            }
            return item;
        });
    };

    const handleDialogClose = () => {
        setIsDialogVisible(false);
    };

    const handleDialogYes = () => {
        if (movieToDelete) {
            setDeleteItemId(movieToDelete.id);
        }
        handleDialogClose();
    };

    return (
        <div>
        {isSaving && (
            <SaveMovieModal
                switchMode={switchSavingMode}
                items={items}
                setItems={setItems}
                movies= {movies}
                setMovies= {setMovies}
            />
        )}
        {isEditing && (
            <EditUserStoryModal
                switchMode={switchEditingMode}
                movie={getSelectedMovie()}
                items={items}
                setItems={setItems}
                movies={movies}
                setMovies={setMovies}
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
            <Dialog
                hidden={!isDialogVisible}
                onDismiss={handleDialogClose}
                dialogContentProps={{
                    type: DialogType.normal,
                    title: '',
                    closeButtonAriaLabel: 'Close',
                    subText: 'Are you sure you want to delete this movie ?',
                }}
                modalProps={{
                    isBlocking: true,
                    styles: { main: { maxWidth: 400 } },
                }}
            >
                <DialogFooter>
                    <Stack horizontal tokens={{ childrenGap: 50 }} horizontalAlign="center">
                    <PrimaryButton onClick={handleDialogYes} text="Yes" />
                    <DefaultButton onClick={handleDialogClose} text="No" />
                    </Stack>
                </DialogFooter>
            </Dialog>
        </div>
    );
};

export default Movies;
