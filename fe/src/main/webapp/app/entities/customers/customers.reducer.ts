import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ICustomers, defaultValue } from 'app/shared/model/customers.model';

export const ACTION_TYPES = {
  FETCH_CUSTOMERS_LIST: 'customers/FETCH_CUSTOMERS_LIST',
  FETCH_CUSTOMERS: 'customers/FETCH_CUSTOMERS',
  CREATE_CUSTOMERS: 'customers/CREATE_CUSTOMERS',
  UPDATE_CUSTOMERS: 'customers/UPDATE_CUSTOMERS',
  PARTIAL_UPDATE_CUSTOMERS: 'customers/PARTIAL_UPDATE_CUSTOMERS',
  DELETE_CUSTOMERS: 'customers/DELETE_CUSTOMERS',
  RESET: 'customers/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ICustomers>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type CustomersState = Readonly<typeof initialState>;

// Reducer

export default (state: CustomersState = initialState, action): CustomersState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_CUSTOMERS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CUSTOMERS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_CUSTOMERS):
    case REQUEST(ACTION_TYPES.UPDATE_CUSTOMERS):
    case REQUEST(ACTION_TYPES.DELETE_CUSTOMERS):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_CUSTOMERS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_CUSTOMERS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CUSTOMERS):
    case FAILURE(ACTION_TYPES.CREATE_CUSTOMERS):
    case FAILURE(ACTION_TYPES.UPDATE_CUSTOMERS):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_CUSTOMERS):
    case FAILURE(ACTION_TYPES.DELETE_CUSTOMERS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_CUSTOMERS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_CUSTOMERS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_CUSTOMERS):
    case SUCCESS(ACTION_TYPES.UPDATE_CUSTOMERS):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_CUSTOMERS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_CUSTOMERS):
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

const apiUrl = 'api/customers';

// Actions

export const getEntities: ICrudGetAllAction<ICustomers> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_CUSTOMERS_LIST,
    payload: axios.get<ICustomers>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<ICustomers> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CUSTOMERS,
    payload: axios.get<ICustomers>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<ICustomers> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_CUSTOMERS,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ICustomers> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_CUSTOMERS,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<ICustomers> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_CUSTOMERS,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ICustomers> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_CUSTOMERS,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
