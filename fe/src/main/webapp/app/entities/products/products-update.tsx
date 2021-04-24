import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ICategories } from 'app/shared/model/categories.model';
import { getEntities as getCategories } from 'app/entities/categories/categories.reducer';
import { getEntity, updateEntity, createEntity, reset } from './products.reducer';
import { IProducts } from 'app/shared/model/products.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IProductsUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ProductsUpdate = (props: IProductsUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { productsEntity, categories, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/products' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getCategories();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...productsEntity,
        ...values,
        categories: categories.find(it => it.id.toString() === values.categoriesId.toString()),
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
          <h2 id="perfumeStoreManagerApp.products.home.createOrEditLabel" data-cy="ProductsCreateUpdateHeading">
            <Translate contentKey="perfumeStoreManagerApp.products.home.createOrEditLabel">Create or edit a Products</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : productsEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="products-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="products-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="productIDLabel" for="products-productID">
                  <Translate contentKey="perfumeStoreManagerApp.products.productID">Product ID</Translate>
                </Label>
                <AvField id="products-productID" data-cy="productID" type="text" name="productID" />
              </AvGroup>
              <AvGroup>
                <Label id="productNameLabel" for="products-productName">
                  <Translate contentKey="perfumeStoreManagerApp.products.productName">Product Name</Translate>
                </Label>
                <AvField id="products-productName" data-cy="productName" type="text" name="productName" />
              </AvGroup>
              <AvGroup>
                <Label id="quantityAvailableLabel" for="products-quantityAvailable">
                  <Translate contentKey="perfumeStoreManagerApp.products.quantityAvailable">Quantity Available</Translate>
                </Label>
                <AvField
                  id="products-quantityAvailable"
                  data-cy="quantityAvailable"
                  type="string"
                  className="form-control"
                  name="quantityAvailable"
                />
              </AvGroup>
              <AvGroup>
                <Label id="priceLabel" for="products-price">
                  <Translate contentKey="perfumeStoreManagerApp.products.price">Price</Translate>
                </Label>
                <AvField id="products-price" data-cy="price" type="string" className="form-control" name="price" />
              </AvGroup>
              <AvGroup>
                <Label id="dateImportLabel" for="products-dateImport">
                  <Translate contentKey="perfumeStoreManagerApp.products.dateImport">Date Import</Translate>
                </Label>
                <AvField id="products-dateImport" data-cy="dateImport" type="date" className="form-control" name="dateImport" />
              </AvGroup>
              <AvGroup>
                <Label id="expireDateLabel" for="products-expireDate">
                  <Translate contentKey="perfumeStoreManagerApp.products.expireDate">Expire Date</Translate>
                </Label>
                <AvField id="products-expireDate" data-cy="expireDate" type="date" className="form-control" name="expireDate" />
              </AvGroup>
              <AvGroup>
                <Label id="descriptionLabel" for="products-description">
                  <Translate contentKey="perfumeStoreManagerApp.products.description">Description</Translate>
                </Label>
                <AvField id="products-description" data-cy="description" type="text" name="description" />
              </AvGroup>
              <AvGroup>
                <Label id="categoryIDLabel" for="products-categoryID">
                  <Translate contentKey="perfumeStoreManagerApp.products.categoryID">Category ID</Translate>
                </Label>
                <AvField id="products-categoryID" data-cy="categoryID" type="text" name="categoryID" />
              </AvGroup>
              <AvGroup>
                <Label id="volumeLabel" for="products-volume">
                  <Translate contentKey="perfumeStoreManagerApp.products.volume">Volume</Translate>
                </Label>
                <AvField id="products-volume" data-cy="volume" type="string" className="form-control" name="volume" />
              </AvGroup>
              <AvGroup>
                <Label for="products-categories">
                  <Translate contentKey="perfumeStoreManagerApp.products.categories">Categories</Translate>
                </Label>
                <AvInput id="products-categories" data-cy="categories" type="select" className="form-control" name="categoriesId">
                  <option value="" key="0" />
                  {categories
                    ? categories.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/products" replace color="info">
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
  categories: storeState.categories.entities,
  productsEntity: storeState.products.entity,
  loading: storeState.products.loading,
  updating: storeState.products.updating,
  updateSuccess: storeState.products.updateSuccess,
});

const mapDispatchToProps = {
  getCategories,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ProductsUpdate);
