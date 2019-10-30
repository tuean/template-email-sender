
const localStorageKey = "email-uuid"
const selectedKey = 'email-selected'
const localStorageSeperate = ";"
const paramKey = 'email-keys';

export const getEmailList = () => {
  let value = localStorage.getItem(localStorageKey)
  if (value == null || value.length < 1) {
    return []
  }

  return JSON.parse(value)
}

export const addItem = (item) => {
  let list = getEmailList()
  list.push(item)
  localStorage.setItem(localStorageKey, JSON.stringify(list))
}

export const updateItem = (item) => {
  let list = getEmailList()
  if (list == null || list.length < 1) return
  let size = list.length
  for (let x = 0; size > x; x++) {
    if (list[x].id === item.id) {
      list[x] = item
    }
  }
  localStorage.setItem(localStorageKey, JSON.stringify(list))
}


export const deleteItem = (id) => {
  let list = getEmailList()
  if (list == null || list.length < 1) return
  let size = list.length
  for (let x = 0; size > x; x++) {
    if (list[x].id === id) {
      list = list.slice(0, x).concat(list.slice(x+1, list.length))
      break
    }
  }

  localStorage.setItem(localStorageKey, JSON.stringify(list))
}

export const getById = (id) => {
  let list = getEmailList()
  if (list == null || list.length < 1) return
  let size = list.length
  for (let x = 0; size > x; x++) {
    if (list[x].id === id) {
      return list[x]
    }
  }
  return []
}

export const getSelect = () => {
  return localStorage.getItem(selectedKey)
}


export const select = (item) => {
  localStorage.setItem(selectedKey, item)
}

export const deleteSelected = () => {
  localStorage.removeItem(selectedKey)
}

const tempForContent = 'email-content'
export const contentStore = (data) => {
  localStorage.setItem(tempForContent, data)
}

export const getTempContent = () => {
  return localStorage.getItem(tempForContent)
}




export const getKeys = () => {
  let value = localStorage.getItem(paramKey)
  if (value == null || value.length < 1) {
    return []
  }

  return JSON.parse(value)
}


export const updateKey = (item) => {
  let list = getKeys()
  let size = list.length
  let updateFlag = false
  for (let x = 0; size > x; x++) {
    if (list[x].name === item.name) {
      list[x] = item
      updateFlag = true
      break
    }
  }

  if (!updateFlag) {
    list.push(item)
  }

  localStorage.setItem(paramKey, JSON.stringify(list))
}

export const deleteKey = (item) => {
  let list = getKeys()
  let size = list.length
  if (size < 1) return
  for (let x = 0; size > x; x++) {
    if (list[x].name === item.name) {
      list = list.slice(0, x).concat(list.slice(x+1, list.length))
      break
    }
  }

  localStorage.setItem(paramKey, JSON.stringify(list))
}
