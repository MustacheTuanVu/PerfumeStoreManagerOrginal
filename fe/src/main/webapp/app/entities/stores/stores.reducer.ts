import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IStores, defaultValue } from 'app/shared/model/stores.model';

export const ACTION_TYPES = {
  FETCH_STORES_LIST: 'stores/FETCH_STORES_LIST',
  FETCH_STORES: 'stores/FETCH_STORES',
  CREATE_STORES: 'stores/CREATE_STORES',
  UPDATE_STORES: 'stores/UPDATE_STORES',
  PARTIAL_UPDATE_STORES: 'stores/PARTIAL_UPDATE_STORES',
  DELETE_STORES: 'stores/DELETE_STORES',
  RESET: 'stores/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IStores>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type StoresState = Readonly<typeof initialState>;

// Reducer

export default (state: StoresState = initialState, action): StoresState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_STORES_LIST):
    case REQUEST(ACTION_TYPES.FETCH_STORES):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_STORES):
    case REQUEST(ACTION_TYPES.UPDATE_STORES):
    case REQUEST(ACTION_TYPES.DELETE_STORES):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_STORES):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_STORES_LIST):
    case FAILURE(ACTION_TYPES.FETCH_STORES):
    case FAILURE(ACTION_TYPES.CREATE_STORES):
    case FAILURE(ACTION_TYPES.UPDATE_STORES):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_STORES):
    case FAILURE(ACTION_TYPES.DELETE_STORES):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_STORES_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_STORES):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_STORES):
    case SUCCESS(ACTION_TYPES.UPDATE_STORES):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_STORES):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_STORES):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/stores';

// Actions

export const getEntities: ICrudGetAllAction<IStores> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_STORES_LIST,
    payload: axios.get<IStores>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IStores> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_STORES,
    payload: axios.get<IStores>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IStores> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_STORES,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IStores> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_STORES,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IStores> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_STORES,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IStores> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_STORES,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
