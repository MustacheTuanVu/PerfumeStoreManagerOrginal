import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IDayWorks, defaultValue } from 'app/shared/model/day-works.model';

export const ACTION_TYPES = {
  FETCH_DAYWORKS_LIST: 'dayWorks/FETCH_DAYWORKS_LIST',
  FETCH_DAYWORKS: 'dayWorks/FETCH_DAYWORKS',
  CREATE_DAYWORKS: 'dayWorks/CREATE_DAYWORKS',
  UPDATE_DAYWORKS: 'dayWorks/UPDATE_DAYWORKS',
  PARTIAL_UPDATE_DAYWORKS: 'dayWorks/PARTIAL_UPDATE_DAYWORKS',
  DELETE_DAYWORKS: 'dayWorks/DELETE_DAYWORKS',
  RESET: 'dayWorks/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IDayWorks>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type DayWorksState = Readonly<typeof initialState>;

// Reducer

export default (state: DayWorksState = initialState, action): DayWorksState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_DAYWORKS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_DAYWORKS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_DAYWORKS):
    case REQUEST(ACTION_TYPES.UPDATE_DAYWORKS):
    case REQUEST(ACTION_TYPES.DELETE_DAYWORKS):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_DAYWORKS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_DAYWORKS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_DAYWORKS):
    case FAILURE(ACTION_TYPES.CREATE_DAYWORKS):
    case FAILURE(ACTION_TYPES.UPDATE_DAYWORKS):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_DAYWORKS):
    case FAILURE(ACTION_TYPES.DELETE_DAYWORKS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_DAYWORKS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_DAYWORKS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_DAYWORKS):
    case SUCCESS(ACTION_TYPES.UPDATE_DAYWORKS):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_DAYWORKS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_DAYWORKS):
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

const apiUrl = 'api/day-works';

// Actions

export const getEntities: ICrudGetAllAction<IDayWorks> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_DAYWORKS_LIST,
    payload: axios.get<IDayWorks>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IDayWorks> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_DAYWORKS,
    payload: axios.get<IDayWorks>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IDayWorks> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_DAYWORKS,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IDayWorks> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_DAYWORKS,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IDayWorks> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_DAYWORKS,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IDayWorks> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_DAYWORKS,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
