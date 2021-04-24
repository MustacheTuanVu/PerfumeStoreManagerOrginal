import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IRoles, defaultValue } from 'app/shared/model/roles.model';

export const ACTION_TYPES = {
  FETCH_ROLES_LIST: 'roles/FETCH_ROLES_LIST',
  FETCH_ROLES: 'roles/FETCH_ROLES',
  CREATE_ROLES: 'roles/CREATE_ROLES',
  UPDATE_ROLES: 'roles/UPDATE_ROLES',
  PARTIAL_UPDATE_ROLES: 'roles/PARTIAL_UPDATE_ROLES',
  DELETE_ROLES: 'roles/DELETE_ROLES',
  RESET: 'roles/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IRoles>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type RolesState = Readonly<typeof initialState>;

// Reducer

export default (state: RolesState = initialState, action): RolesState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_ROLES_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ROLES):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_ROLES):
    case REQUEST(ACTION_TYPES.UPDATE_ROLES):
    case REQUEST(ACTION_TYPES.DELETE_ROLES):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_ROLES):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_ROLES_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ROLES):
    case FAILURE(ACTION_TYPES.CREATE_ROLES):
    case FAILURE(ACTION_TYPES.UPDATE_ROLES):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_ROLES):
    case FAILURE(ACTION_TYPES.DELETE_ROLES):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_ROLES_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_ROLES):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_ROLES):
    case SUCCESS(ACTION_TYPES.UPDATE_ROLES):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_ROLES):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_ROLES):
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

const apiUrl = 'api/roles';

// Actions

export const getEntities: ICrudGetAllAction<IRoles> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_ROLES_LIST,
    payload: axios.get<IRoles>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IRoles> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ROLES,
    payload: axios.get<IRoles>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IRoles> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ROLES,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IRoles> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ROLES,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IRoles> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_ROLES,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IRoles> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ROLES,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
