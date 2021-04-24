import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IBills, defaultValue } from 'app/shared/model/bills.model';

export const ACTION_TYPES = {
  FETCH_BILLS_LIST: 'bills/FETCH_BILLS_LIST',
  FETCH_BILLS: 'bills/FETCH_BILLS',
  CREATE_BILLS: 'bills/CREATE_BILLS',
  UPDATE_BILLS: 'bills/UPDATE_BILLS',
  PARTIAL_UPDATE_BILLS: 'bills/PARTIAL_UPDATE_BILLS',
  DELETE_BILLS: 'bills/DELETE_BILLS',
  RESET: 'bills/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IBills>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type BillsState = Readonly<typeof initialState>;

// Reducer

export default (state: BillsState = initialState, action): BillsState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_BILLS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_BILLS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_BILLS):
    case REQUEST(ACTION_TYPES.UPDATE_BILLS):
    case REQUEST(ACTION_TYPES.DELETE_BILLS):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_BILLS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_BILLS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_BILLS):
    case FAILURE(ACTION_TYPES.CREATE_BILLS):
    case FAILURE(ACTION_TYPES.UPDATE_BILLS):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_BILLS):
    case FAILURE(ACTION_TYPES.DELETE_BILLS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_BILLS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_BILLS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_BILLS):
    case SUCCESS(ACTION_TYPES.UPDATE_BILLS):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_BILLS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_BILLS):
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

const apiUrl = 'api/bills';

// Actions

export const getEntities: ICrudGetAllAction<IBills> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_BILLS_LIST,
    payload: axios.get<IBills>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IBills> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_BILLS,
    payload: axios.get<IBills>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IBills> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_BILLS,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IBills> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_BILLS,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IBills> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_BILLS,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IBills> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_BILLS,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
