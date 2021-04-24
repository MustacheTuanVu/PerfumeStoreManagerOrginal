import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './categories.reducer';
import { ICategories } from 'app/shared/model/categories.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ICategoriesUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const CategoriesUpdate = (props: ICategoriesUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { categoriesEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/categories' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...categoriesEntity,
        ...values,
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="perfumeStoreManagerApp.categories.home.createOrEditLabel" data-cy="CategoriesCreateUpdateHeading">
            <Translate contentKey="perfumeStoreManagerApp.categories.home.createOrEditLabel">Create or edit a Categories</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : categoriesEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="categories-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="categories-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="categoryIDLabel" for="categories-categoryID">
                  <Translate contentKey="perfumeStoreManagerApp.categories.categoryID">Category ID</Translate>
                </Label>
                <AvField id="categories-categoryID" data-cy="categoryID" type="text" name="categoryID" />
              </AvGroup>
              <AvGroup>
                <Label id="categoryNameLabel" for="categories-categoryName">
                  <Translate contentKey="perfumeStoreManagerApp.categories.categoryName">Category Name</Translate>
                </Label>
                <AvField id="categories-categoryName" data-cy="categoryName" type="text" name="categoryName" />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/categories" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  categoriesEntity: storeState.categories.entity,
  loading: storeState.categories.loading,
  updating: storeState.categories.updating,
  updateSuccess: storeState.categories.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CategoriesUpdate);
