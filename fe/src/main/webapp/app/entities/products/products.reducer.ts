import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IProducts, defaultValue } from 'app/shared/model/products.model';

export const ACTION_TYPES = {
  FETCH_PRODUCTS_LIST: 'products/FETCH_PRODUCTS_LIST',
  FETCH_PRODUCTS: 'products/FETCH_PRODUCTS',
  CREATE_PRODUCTS: 'products/CREATE_PRODUCTS',
  UPDATE_PRODUCTS: 'products/UPDATE_PRODUCTS',
  PARTIAL_UPDATE_PRODUCTS: 'products/PARTIAL_UPDATE_PRODUCTS',
  DELETE_PRODUCTS: 'products/DELETE_PRODUCTS',
  RESET: 'products/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IProducts>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type ProductsState = Readonly<typeof initialState>;

// Reducer

export default (state: ProductsState = initialState, action): ProductsState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_PRODUCTS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PRODUCTS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_PRODUCTS):
    case REQUEST(ACTION_TYPES.UPDATE_PRODUCTS):
    case REQUEST(ACTION_TYPES.DELETE_PRODUCTS):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_PRODUCTS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_PRODUCTS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PRODUCTS):
    case FAILURE(ACTION_TYPES.CREATE_PRODUCTS):
    case FAILURE(ACTION_TYPES.UPDATE_PRODUCTS):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_PRODUCTS):
    case FAILURE(ACTION_TYPES.DELETE_PRODUCTS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_PRODUCTS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_PRODUCTS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_PRODUCTS):
    case SUCCESS(ACTION_TYPES.UPDATE_PRODUCTS):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_PRODUCTS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_PRODUCTS):
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

const apiUrl = 'api/products';

// Actions

export const getEntities: ICrudGetAllAction<IProducts> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_PRODUCTS_LIST,
    payload: axios.get<IProducts>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IProducts> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PRODUCTS,
    payload: axios.get<IProducts>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IProducts> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PRODUCTS,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IProducts> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PRODUCTS,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IProducts> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_PRODUCTS,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IProducts> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PRODUCTS,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
