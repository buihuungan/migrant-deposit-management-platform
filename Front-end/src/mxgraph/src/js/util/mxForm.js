/**
 * Copyright (c) 2006-2015, JGraph Ltd
 * Copyright (c) 2006-2015, Gaudenz Alder
 */
/**
 * Class: mxForm
 *
 * A simple class for creating HTML forms.
 *
 * Constructor: mxForm
 *
 * Creates a HTML table using the specified classname.
 */
function mxForm(className) {
	this.table = document.createElement('table')
  this.table.style.width = '100%'
	this.table.className = className
  this.table.setAttribute('id', className)
	this.body = document.createElement('tbody')

	this.table.appendChild(this.body)
};

/**
 * Variable: table
 *
 * Holds the DOM node that represents the table.
 */
mxForm.prototype.table = null

/**
 * Variable: body
 *
 * Holds the DOM node that represents the tbody (table body). New rows
 * can be added to this object using DOM API.
 */
mxForm.prototype.body = false

/**
 * Function: getTable
 *
 * Returns the table that contains this form.
 */
mxForm.prototype.getTable = function() {
	return this.table
}

mxForm.prototype.addHeader = function(name, title) {
  let tr = document.createElement('tr')
	mxUtils.write(tr, title)
  tr.setAttribute('class', `${name} mxWindowTr`)

  this.body.appendChild(tr)
}

mxForm.prototype.addSelect = function(name, title, placeholder, records, key, hasmodule, key1, key2) {
  let tr = document.createElement('tr')
  tr.className = 'mxWindowTr'

  this.body.appendChild(tr)

  let td = document.createElement('td')

	mxUtils.write(td, title)
  tr.appendChild(td)

  td = document.createElement('td')
  tr.appendChild(td)
  let content = document.createElement('div')
  content.setAttribute('id', `${name}_content`)
  content.setAttribute('class', 'content')
  content.setAttribute('placeholder', `请选择对应的${placeholder}`)

  if (hasmodule) {
    content.setAttribute('value', hasmodule.id)
    if (key1) {
      content.setAttribute(key1, hasmodule[`${key1}`])
    }
    if (key2) {
      content.setAttribute(key2, hasmodule[`${key2}`])
    }

    content.innerText = hasmodule.value
  }

  td.appendChild(content)

  let selectItem = document.createElement('div')
  selectItem.setAttribute('id', 'selectItem')
  td.appendChild(selectItem)

  let ul = document.createElement('ul')
  selectItem.appendChild(ul)
  if (records && records.length) {
    for (let i = 0; i < records.length; i++) {
      let li = document.createElement('li')
      li.setAttribute('class', `${name}`)
      li.setAttribute('value', records[i].id)

      if (key1) {
        content.setAttribute(key1, records[`${key1}`])
      }
      if (key2) {
        content.setAttribute(key2, records[`${key2}`])
      }

      mxUtils.write(li, records[i][`${key}`])

      ul.appendChild(li)
    }
  }

  let i = document.createElement('span')
  i.className = 'ant-menu-submenu-arrow'
  i.innerHTML = '<svg viewBox="64 64 896 896" data-icon="down" width="1em" height="1em" fill="currentColor" aria-hidden="true" focusable="false" class=""><path d="M884 256h-75c-5.1 0-9.9 2.5-12.9 6.6L512 654.2 227.9 262.6c-3-4.1-7.8-6.6-12.9-6.6h-75c-6.5 0-10.3 7.4-6.5 12.7l352.6 486.1c12.8 17.6 39 17.6 51.7 0l352.6-486.1c3.9-5.3.1-12.7-6.4-12.7z"></path></svg>'
  content.appendChild(i)

  /**
   * 点击下拉箭头
   */

  content.onclick = function (e) {
    e.preventDefault()
    if (selectItem.style.display == 'none' || selectItem.style.display == '') {
        selectItem.style.display = 'block'
    } else {
        selectItem.style.display = 'none'
    }
  }

  var lis = selectItem.querySelectorAll(`.${name}`)
  if (lis && lis.length) {
    for (let i = 0; i < lis.length; i++) {
      lis[i].onclick = function () {
        content.innerText = this.innerHTML
        content.setAttribute('value', this.getAttribute('value'))
        // selectItem.style.display = 'none'

        let i = document.createElement('span')
        i.className = 'ant-menu-submenu-arrow'
        i.innerHTML = '<svg viewBox="64 64 896 896" data-icon="down" width="1em" height="1em" fill="currentColor" aria-hidden="true" focusable="false" class=""><path d="M884 256h-75c-5.1 0-9.9 2.5-12.9 6.6L512 654.2 227.9 262.6c-3-4.1-7.8-6.6-12.9-6.6h-75c-6.5 0-10.3 7.4-6.5 12.7l352.6 486.1c12.8 17.6 39 17.6 51.7 0l352.6-486.1c3.9-5.3.1-12.7-6.4-12.7z"></path></svg>'
        content.appendChild(i)
      }
    }
  }
}

mxForm.prototype.addPerson = function(name, records, hasroles) {
  let tr_table = document.createElement('tr')

  let title = document.createElement('div')
  title.setAttribute('class', `${name}_title`)
  mxUtils.write(title, '用户角色:')
  tr_table.appendChild(title)

  let table = document.createElement('table')

  tr_table.appendChild(table)
  table.setAttribute('class', 'ttable-bordered')

  let thead = document.createElement('thead')
  let tr = document.createElement('tr')
  tr.style.background = '#fbfbfb'

  let td = document.createElement('td')
	mxUtils.write(td, '用户名或角色名')
  tr.appendChild(td)

  td = document.createElement('td')
	mxUtils.write(td, '选择')
  tr.appendChild(td)

  thead.appendChild(tr)
  table.appendChild(thead)

  this.body.appendChild(tr_table)

  let tbody = document.createElement('tbody')
  if (records && records.length) {
    for (let i = 0; i < records.length; i++) {
      let tr1 = document.createElement('tr')

      let td1 = document.createElement('td')
      mxUtils.write(td1, records[i].roleName)
      tr1.appendChild(td1)

      td1 = document.createElement('td')
      var input = document.createElement('input')
      td1.appendChild(input)
      input.setAttribute('class', 'person_checkbox')

      input.setAttribute('type', 'checkbox')

      for (let j = 0; j < hasroles.length; j++) {
        if (hasroles[j].id == records[i].id) {
          input.setAttribute('checked', true)
          break
        }
      }

      input.setAttribute('value', records[i].id)

      tr1.appendChild(td1)

      tbody.appendChild(tr1)
    }
  }

  table.appendChild(tbody)

  // let tbody = document.createElement('tbody')
}

/**
 * Function: addButtons
 *
 * Helper method to add an OK and Cancel button using the respective
 * functions.
 */
mxForm.prototype.addButtons = function(okFunct, cancelFunct) {
	var tr = document.createElement('tr')
  tr.className = 'mxWindowTr'
	var td = document.createElement('td')
  tr.style.width = 380 + 'px'
  tr.style.display = 'flex'
  tr.style.justifyContent = 'space-around'

  td.style.fontWeight = 700

  // Adds the ok button
	var button = document.createElement('button')
  button.style.width = '100px'
  button.className = 'btn-primary button'
	mxUtils.write(button, mxResources.get('ok') || 'OK')
	td.appendChild(button)

	mxEvent.addListener(button, 'click', function() {
		okFunct()
	})

	tr.appendChild(td)
	td = document.createElement('td')

	// Adds the cancel button
	button = document.createElement('button')
  button.className = 'button'

  button.style.width = '100px'

	mxUtils.write(button, mxResources.get('cancel') || 'Cancel')
	td.appendChild(button)

	mxEvent.addListener(button, 'click', function() {
		cancelFunct()
	})

	tr.appendChild(td)
	this.body.appendChild(tr)
}

/**
 * Function: addText
 *
 * Adds an input for the given name, type and value and returns it.
 */
mxForm.prototype.addText = function(name, key, type) {
	var input = document.createElement('input')

	input.setAttribute('type', type || 'text')
  input.setAttribute('id', key)

	return this.addField(name, input)
}

/**
 * Function: addCheckbox
 *
 * Adds a checkbox for the given name and value and returns the textfield.
 */
mxForm.prototype.addCheckbox = function(name, value) {
	var input = document.createElement('input')

	input.setAttribute('type', 'checkbox')
	this.addField(name, input)

	// IE can only change the checked value if the input is inside the DOM
	if (value) {
		input.checked = true
	}

	return input
}

/**
 * Function: addTextarea
 *
 * Adds a textarea for the given name and value and returns the textarea.
 */
mxForm.prototype.addTextarea = function(name, value, rows) {
	var input = document.createElement('textarea')

	if (mxClient.IS_NS) {
		rows--
	}

	input.setAttribute('rows', rows || 2)
	input.value = value

	return this.addField(name, input)
}

/**
 * Function: addCombo
 *
 * Adds a combo for the given name and returns the combo.
 */
mxForm.prototype.addCombo = function(name, isMultiSelect, size, id) {
	var select = document.createElement('select')
  select.setAttribute('id', id)

	// if (size != null) {
	// 	select.setAttribute('size', size)
	// }

	if (isMultiSelect) {
		select.setAttribute('multiple', 'multiple')
	}

  return this.addField(name, select)
}

/**
 * Function: addOption
 *
 * Adds an option for the given label to the specified combo.
 */
mxForm.prototype.addOption = function(combo, label, value, isSelected) {
	var option = document.createElement('option')

	mxUtils.writeln(option, label)
	option.setAttribute('value', value)

	if (isSelected) {
		option.setAttribute('selected', isSelected)
	}
  console.log('添加数据', combo, option)

	combo.appendChild(option)
}

/**
 * Function: addField
 *
 * Adds a new row with the name and the input field in two columns and
 * returns the given input.
 */
mxForm.prototype.addField = function(name, input) {
  input.style.marginLeft = 10 + 'px'
  input.style.width = 280 + 'px'
	var tr = document.createElement('tr')
  tr.className = 'mxWindowTr'

	var td = document.createElement('td')
  tr.style.width = 380 + 'px'

  td.style.fontWeight = 700

	mxUtils.write(td, name)
	tr.appendChild(td)

	td = document.createElement('td')

	td.appendChild(input)
	tr.appendChild(td)
	this.body.appendChild(tr)

	return input
}
